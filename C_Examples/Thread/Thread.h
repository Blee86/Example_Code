//
//  Thread.h
//  Thread
//
//  Created by Yosub Lee on 7/6/14.
//  Copyright (c) 2014 Yosub Lee. All rights reserved.
//
//  This code is based on the article "Java Style Thread Class in C++"
//  http://vichargrave.com/java-style-thread-class-in-c/

#ifndef Thread_Thread_h
#define Thread_Thread_h

#include <pthread.h>

class Thread {
public:
    Thread();
    virtual ~Thread();
    
    int start();
    int join();
    int detach();
    pthread_t self();
    
    virtual void* run() = 0;
    
private:
    pthread_t m_tid;        // Thread id
    int m_running;          // Flag: 0 - not running, 1 - running
    int m_detached;         // Flag: 0 - thread is not detached, 1 - is detached
};

#endif
