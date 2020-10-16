package kr.co.takeit.exception;

public class TakeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TakeException(){
		
	}

	public TakeException(String msg){
		super(msg);
	}
	
	public TakeException(Throwable throwable)
    {
        super(throwable.getMessage(), throwable);
    }
	
	public TakeException(String msg, Throwable throwable)
    {
        super(msg, throwable);
    }
	
	public String getErrorMessage(){
		return super.getMessage();
	}
}
