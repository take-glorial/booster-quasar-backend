package kr.co.company.framework.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kr.co.takeit.common.service.FileService;
import kr.co.takeit.spring.dao.TakeDAO;
import kr.co.takeit.util.TakeStringUtil;

@Service
public class CmService {

	private static final Logger logger = LoggerFactory.getLogger(CmService.class);

	@Autowired
	FileService fileService;

	@Autowired
	private TakeDAO dao;

	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void boardInfoInsert(Map<String, Object> noticeInfo, List<Map<String, Object>> targetList) {
		if(!CollectionUtils.isEmpty(noticeInfo)) {
			dao.insert("BoardManagement.boardInfoInsert", noticeInfo);
		}

		if (!CollectionUtils.isEmpty(targetList) ) {
			for(Map<String, Object> targetInfo : targetList) {
				targetInfo.put("board_seq", TakeStringUtil.nvl(noticeInfo.get("board_seq")));
				dao.insert("BoardManagement.boardTargetInsert", targetInfo);
			}
		}
	}

	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void boardInfoUpdate(Map<String, Object> noticeInfo, List<Map<String, Object>> targetList) {
		dao.update("BoardManagement.boardInfoUpdate", noticeInfo);

		if (!CollectionUtils.isEmpty(targetList) ) {
			for(Map<String, Object> targetInfo : targetList) {
				targetInfo.put("board_seq", TakeStringUtil.nvl(noticeInfo.get("board_seq")));
				dao.insert("BoardManagement.boardTargetInsert", targetInfo);
				dao.delete("BoardManagement.boardTargetDelete", targetInfo);
			}
		}
	}

	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void boardInfoDelete(Map<String, Object> paramMap) {
		dao.delete("BoardManagement.boardReplyDelete"	, paramMap);	// 댓글정보 삭제
		dao.delete("BoardManagement.boardReaduserDelete", paramMap); 	// 조회사용자 삭제
		dao.delete("BoardManagement.boardTargetDelete"	, paramMap);	// 게시판공지대상 삭제
		dao.delete("BoardManagement.boardInfoDelete"	, paramMap);	// 게시판 삭제

		//첨부파일 삭제
		//fileService.deleteAll(sAttachCd);
	}

	/**
	 * 조회수 증가
	 *
	 * @param paramMap
	 */
	@Transactional (value="ChainedTxManager", rollbackFor = { Exception.class })
	public void readUser(Map<String, Object> paramMap) {
		if(dao.update("BoardManagement.boardReaduserUpdate", paramMap)==0) {
			dao.insert("BoardManagement.boardReaduserInsert", paramMap);
		}
	}

}
