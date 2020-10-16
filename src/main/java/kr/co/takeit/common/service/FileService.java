package kr.co.takeit.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.takeit.exception.TakeException;
import kr.co.takeit.spring.dao.TakeVueDAO;
import kr.co.takeit.util.SessionManager;
import kr.co.takeit.util.TakeFileUtil;
import kr.co.takeit.util.TakeStringUtil;

@Service
public class FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	TakeVueDAO takeVueDao;

	@Autowired
	SessionManager sessionMgr;

	/**
	 * 파일 업로드 경로
	 */
	@Value("${file.upload.path}")
	private String fileUploadPath;

	/**
	 * 서버 임시 경로
	 */
	@Value("${tmp.upload.path}")
	private String tmpUploadPath;

	/**
	 * DB 저장 경로
	 */
	//@Value("${db.upload.path}")
	private String dbUploadPath;

	/**
	 * 업로드 하위 경로 추출
	 * \첨부_유형\Y연도\M월
	 *
	 * @return saveDirPath
	 */
	public String getFileUploadRelativePath(String fileType) {
		return fileType + "/" + "Y" + TakeStringUtil.format("yyyy", new Date()) + "/" + "M" + TakeStringUtil.format("MM", new Date());
	}

	/**
	 * 고정경로 추가
	 *
	 * @param filePath
	 * @return
	 */
	public String setPrefixPath(String filePath) {
		if(filePath.indexOf(dbUploadPath) == -1) {
			return dbUploadPath + filePath;
		} else {
			return filePath;
		}
	}

	/**
	 * 고정경로 제거
	 *
	 * @param filePath
	 * @return
	 */
	public String removePrefixPath(String filePath) {
		return TakeStringUtil.replace(filePath, dbUploadPath, "");
	}

	/**
	 * 파일 전체삭제
	 *
	 * @param filelist
	 * @throws IOException
	 */
	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void deleteAll(String attachCd) {
		Map<String, String> fileParams = new HashMap<String, String>();
		fileParams.put("attach_cd", attachCd);

		List<LinkedHashMap<String, Object>> filelist = takeVueDao.select("File.attachInfoSelect", fileParams);
		this.deleteAll(filelist);
	}

	/**
	 * 파일 전체삭제
	 *
	 * @param filelist
	 * @throws Exception
	 */
	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void deleteAll(List<LinkedHashMap<String, Object>> filelist) {

		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		String sessionUserCd = sessionMgr.getUserCd(attr.getRequest());

		for(Map<String, Object> fileInfo : filelist) {
			fileInfo.put("_sessionUserCd", sessionUserCd);

			takeVueDao.update("File.attachInfoFileDelete", fileInfo);

			//첨부파일 삭제
			try {
				deletePhysicalFile(fileInfo);
			} catch (Exception e) {
				//에러 방지
				logger.error("첨부파일 삭제 오류", e);
			}
		}
	}

	/**
	 * 물리 파일 삭제
	 *
	 * @param fileInfo
	 * @throws TakeException
	 */
	private void deletePhysicalFile(Map<String, Object> fileInfo) throws Exception {
		String filePath = fileUploadPath + File.separator + (String)fileInfo.get("server_path") + File.separator;
		String fileName = (String)fileInfo.get("server_file_name");

		File deleteFile = new File(TakeFileUtil.generalizePath(filePath + fileName));
		if(deleteFile.exists() && deleteFile.isFile()) {
			TakeFileUtil.deleteFile(deleteFile);
		}
	}

	/**
	 * ATTACH_INFO 신규첨부파일코드 생성 후 조회
	 *
	 * @param params
	 * @return
	 */
	public Map<String, String> getAtchFileId(Map<String, String> params) {
		int result = takeVueDao.insert("File.attachInfoInsert", params);
		if(result > 0) {
			Map<String, String> atchFileInfo = new HashMap<>();
			atchFileInfo.put("atchFileId", TakeStringUtil.nvl(params.get("maxCd")));
			return atchFileInfo;
		}

		return null;
	}

	/**
	 * ATTACH_INFO 파일 목록 조회
	 *
	 * @param params
	 * @return
	 */
	public List<LinkedHashMap<String, Object>> getAtchFileList(Map<String, Object> params) {
		return takeVueDao.select("File.attachInfoSelect", params);
	}

	/**
	 * ATTACH_INFO 파일 업로드
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> uploadAtchFile(HttpServletRequest request) throws Exception {
		String projectCd		= TakeStringUtil.nvl(request.getParameter("projectCd"));
		String menuCd			= TakeStringUtil.nvl(request.getParameter("menuCd"));
		String atchFileId		= TakeStringUtil.nvl(request.getParameter("atchFileId"));

		MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;

		String saveDirPath = fileUploadPath + File.separator + projectCd + File.separator + menuCd + File.separator + atchFileId + File.separator;
			   saveDirPath = TakeFileUtil.generalizePath(saveDirPath);

		TakeFileUtil.ensureDirectory(saveDirPath);
		logger.debug("upload경로:{}", saveDirPath);

		List<Map<String, Object>> fileList = new ArrayList<Map<String,Object>>();

		Iterator<String> iter = mreq.getFileNames();
		while(iter.hasNext()) {
			String fileName = iter.next();

			MultipartFile mFile = mreq.getFile(fileName);
			mFile.transferTo(new File(saveDirPath + mFile.getName()));

			logger.debug(fileName + "::::::::" + mFile.getOriginalFilename());

			Map<String, Object> fileInfo = new HashMap<String, Object>();
			fileInfo.put("file_save_nm"	, mFile.getName());
			fileInfo.put("file_path"	, File.separator + projectCd + File.separator + menuCd + File.separator + atchFileId);

			fileList.add(fileInfo);
		}

		return fileList;
	}

	/**
	 * ATTACH_INFO 파일 추가/삭제
	 *
	 * @param filelist
	 * @throws TakeException
	 */
	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void saveAtchFile(List<Map<String, Object>> filelist){
		for(Map<String, Object> fileInfo : filelist) {
			int rowType = (int)fileInfo.get("_rowtype_");

//			if(rowType == DataSet.ROW_TYPE_INSERTED) {
//				takeVueDao.insert("File.attachInfoFileInsert", fileInfo);
//			}else if(rowType == DataSet.ROW_TYPE_UPDATED) {
//				//takeVueDao.update("File.attachInfoFileUpdate", fileInfo);
//			}else if(rowType == DataSet.ROW_TYPE_DELETED) {
//				takeVueDao.update("File.attachInfoFileDelete", fileInfo);
//
//				//첨부파일 삭제
//				try {
//					deletePhysicalFile(fileInfo);
//				} catch (Exception e) {
//					//에러 방지
//					logger.error("첨부파일 삭제 오류", e);
//				}
//			}
		}
	}

	/**
	 * 다운로드 처리
	 *
	 * @param response
	 * @param filePath
	 * @param downName
	 * @throws IOException
	 */
	public void download(HttpServletResponse response, String filePath, String downName) throws IOException{
		response.reset();
		File f = new File(filePath);
		long len = f.length();

		/*String browser = request.getHeader("User-Agent"); //파일 인코딩
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){
			downName = URLEncoder.encode(downName, StandardCharsets.UTF_8.name());
		}else{
			downName = new String(downName.getBytes(StandardCharsets.UTF_8.name()), StandardCharsets.ISO_8859_1.name());
		}
		downName = downName.replaceAll("\\+", " ");
		response.setContentType("application/octet-stream");
		response.setContentLength((int) len);
		response.setHeader("content-disposition","attachment;filename=\""+downName+ "\"");*/

		downName = URLEncoder.encode(downName,"UTF-8").replace("+", "%20");

		response.setContentType("application/octet-stream");
		response.setContentLength((int) len);
		response.setHeader("Content-Disposition", "attachment;filename=" + downName + ";filename*= UTF-8''" + downName);

		try(
			FileInputStream fis = new FileInputStream(f);
			ServletOutputStream os = response.getOutputStream()
		){
			FileCopyUtils.copy(fis, os);
			os.flush();
		} catch (IOException ioe) {
			throw ioe;
		}
	}

	/**
	 * 파일삭제 스케쥴러
	 * 매일 새벽 1시에 7일 경과한(지난주) 임시파일 삭제
	 */
	//@Scheduled(cron = "0 0 1 * * *")
	public void deleteScheduledJob() {
		logger.info("temporary path deleteScheduledJob start : {}", tmpUploadPath);
		try {
			deleteFile(60*60*24*5, tmpUploadPath);
		} catch (Exception e) {
		}
		logger.info("deleteScheduledJob finished");
	}

	/**
	 * 폴더안의 파일 삭제 후 폴더 삭제여부 판단
	 * @param dir
	 * @param directoryDelete
	 * @throws IOException
	 */
	public void deleteFile( int sec, String dir) throws IOException {
		File f = new File( TakeFileUtil.generalizePath(dir) );

		if(f!=null && f.isDirectory()) {
			File[] list = f.listFiles();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (list[i].isDirectory()) {
						// 재귀호출
						deleteFile(sec, dir + "/" + list[i].getName());
					} else {
						long fModify = getModifiedTime(list[i]);
						if (fModify > sec) {
							list[i].delete();
							logger.info("temp file delete : {},  modify time(s) : ", new Object[] {list[i].getName(), fModify} );
						}
					}
				}
			}
		}
	}

	/**
	 * 파일 수정일자 조회
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private long getModifiedTime(File f) throws IOException {
		Path attribPath = f.toPath();
		BasicFileAttributes basicAttribs = Files.readAttributes(attribPath, BasicFileAttributes.class);
		return (System.currentTimeMillis() - basicAttribs.lastModifiedTime().to(TimeUnit.MILLISECONDS)) / 1000;
	}
}