package com.usmanadio.banka.services.utils;

public class AccountNumberCreator {

    public String createAcctNumber() {
        int m = (int) Math.pow(10, 8 - 1);
        int randomNumber = m + new java.util.Random().nextInt(9 * m);
        return "00" + randomNumber;
    }
}
