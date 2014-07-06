//
//  main.cpp
//  Thread
//
//  Created by Yosub Lee on 7/6/14.
//  Copyright (c) 2014 Yosub Lee. All rights reserved.
//

#include<stdio.h>
#include<iostream>
#include "Thread.h"
#include "Test.h"

int main(int argc, char** argv) {
    Test* temp1 = new Test();
    Test* temp2 = new Test();
    
    temp1->start();
    temp2->start();
    temp1->join();
    temp2->join();
    
    std::cout << "Done" << std::endl;
    exit(0);
}