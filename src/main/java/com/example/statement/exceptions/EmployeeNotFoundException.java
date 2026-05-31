package com.example.statement.exceptions;

public class EmployeeNotFoundException extends RuntimeException
{
    public EmployeeNotFoundException(String message)
    {
        super(message);
    }
}
