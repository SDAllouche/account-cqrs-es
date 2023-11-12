package ma.sdia.comptcqrses.commonapi.execptions;

public class NegativeInitialBalanceException extends RuntimeException {
    public NegativeInitialBalanceException(String message) {
        super(message);
    }
}
