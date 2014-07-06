//
//  Test.cpp
//  Thread
//
//  Created by Yosub Lee on 7/6/14.
//  Copyright (c) 2014 Yosub Lee. All rights reserved.
//

#include "Test.h"

Test::Test() {}
Test::~Test(){}

void *Test::run() {
    
    for ( int i = 0 ; i < 5 ; i++ ) {
        std::cout << "Thread: " << self() << " / "  << i << std::endl;
        sleep(2);
    }
    
    std::cout << "Thread is done\n";
    return NULL;
}