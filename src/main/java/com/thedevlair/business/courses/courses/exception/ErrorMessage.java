package com.thedevlair.business.courses.courses.exception;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message) {
}
