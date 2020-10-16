package kr.co.takeit.util;

public enum ResponseCode {
	LOGIN_NOT_FOUND("UserId or Password incorrect"),
	EXCEPTION_IO_PARSER("Exception");

	private String value;

	ResponseCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

	public static ResponseCode getEnum(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		for (ResponseCode v : values())
			if (value.equalsIgnoreCase(v.getValue()))
				return v;
		throw new IllegalArgumentException();
	}
}
