//
//  Test.h
//  Thread
//
//  Created by Yosub Lee on 7/6/14.
//  Copyright (c) 2014 Yosub Lee. All rights reserved.
//

#ifndef __Thread__Test__
#define __Thread__Test__

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "Thread.h"

class Test : public Thread
{
public:
    Test();
    ~Test();
    void *run();
};
#endif /* defined(__Thread__Test__) */
