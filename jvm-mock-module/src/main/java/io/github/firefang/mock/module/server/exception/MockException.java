package io.github.firefang.mock.module.server.exception;

public class MockException extends Exception {
    private static final long serialVersionUID = 1L;
    private String conflictClassPattern;
    private String conflictMethodPattern;

    public MockException(String conflictClassPattern, String conflictMethodPattern) {
        super();
        this.conflictClassPattern = conflictClassPattern;
        this.conflictMethodPattern = conflictMethodPattern;
    }

    public String getConflictClassPattern() {
        return conflictClassPattern;
    }

    public String getConflictMethodPattern() {
        return conflictMethodPattern;
    }

}
