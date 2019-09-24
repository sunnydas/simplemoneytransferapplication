package com.transfer.money.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class LockManager {

    private Lock accountUserLock;
    private Lock accountLock;

    private static LockManager lockManager = new LockManager();

    private LockManager() {
        this.accountUserLock = new ReentrantLock();
        this.accountLock = new ReentrantLock();
    }

    public static LockManager getLockManager(){
        return lockManager;
    }

    public void lockAccountUserTx(){
        this.accountUserLock.lock();
    }

    public void lockAccountTx(){
        this.accountLock.lock();
    }

    public void unLockAccountUserTx(){
        this.accountUserLock.unlock();
    }

    public void unlockAccountTx(){
        this.accountLock.unlock();
    }

}
