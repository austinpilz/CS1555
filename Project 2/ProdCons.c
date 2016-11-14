//
//  ProdCons.c
//  
//
//  Created by Austin Pilz on 10/16/16.
//
//

//Includes
#include <linux/unistd.h>
#include <sys/mman.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>


struct cs1550_sem {
    int value;
    struct Node *first;
    struct Node *last;
};

//Wrapper Functions//
void down(struct cs1550_sem *sem) {
    syscall(__NR_cs1550_down, sem);
}
void up(struct cs1550_sem *sem) {
    syscall(__NR_cs1550_up, sem);
}



int main(int argc, char *argv[])
{
    int numChefs, int numCustomers, int bufferSize, int waitStatus;
    
    //Check to see that the required arguments were provided
    if(argc != 4) {
        printf("Syntax error: ./prodcons [#chefs] [#customers] [bufferSize]");
        exit(1);
    }
    else
    {
        numChefs = atoi(argv[1]);
        numCustomers = atoi(argv[2]);
        bufferSize = atoi(argv[3]);
    }
    
    /* OS RAM Request
     void *ptr = mmap(NULL, N, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
     */
    
    void *structPointer = mmap(NULL, sizeof(struct cs1550_sem)*3, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
    void *sharedBuffer = mmap(NULL, sizeof(int)*(bufferSize + 1), PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
    
    struct cs1550_sem *empty = (struct cs1550_sem*)structPointer;
    struct cs1550_sem *filled = (struct cs1550_sem*)structPointer + 1; //offset by 1 struct
    struct cs1550_sem *mutualExclusion = (struct cs1550_sem*)structPointer + 2; //offset by 2 struct
    

    int *count = (int*)sharedBuffer;
    int *chefPosition = (int*)sharedBuffer + 1;
    int *customerPosition = (int*)sharedBuffer + 2;
    int *bufferLocation = (int*)sharedBuffer + 3;
    
    //Initialize Empty Semaphore
    empty->value = bufferSize;
    empty->first = NULL;
    empty->last = NULL;
    
    //Initialize Filled Semaphore
    filled->value = 0;
    filled->first = NULL;
    filled->last = NULL;
    
    //Initialize Mutual Exclusion Semaphore
    mutualExclusion->value = 1;
    mutualExclusion->first = NULL;
    mutualExclusion->last = NULL;
    
    //Init
    *count = bufferSize;
    *chefPosition = 0;
    *customerPosition = 0;
    
    int x;
    for(x = 0; x < numChefs; x++)
    {
        if(fork() == 0)
        {
            //If it's a child process
            
            int chef;
            while(1)
            {
                down(empty);
                down(mutualExclusion);
                //Semaphore Locked
                    chef = *chefPosition;
                    *chefPosition = (*chefPosition+1) % *count;
                    bufferLocation[*chefPosition] = chef;
                
                    printf("Chef %c Produced: Pancake%d\n", x+65, chef); //ASCII - # + 65 = letter
                //Unlock Semaphore
                up(mutualExclusion);
                up(filled);
            }
        }
    }
    
    for(x = 0; x < numCustomers; x++)
    {
        if(fork() == 0)
        {
            //If it's a child process
            
            int customer;
            while(1)
            {
                down(filled);
                down(mutualExclusion);
                //Semaphore Locked
                    customer = bufferLocation[*customerPosition];
                    *customerPosition = (*customerPosition+1) % *count;
                
                    printf("Consumer %c Consumed: Pancake%d\n", x+65, customer); //ASCII - # + 65 = letter
                //Unlock Semaphore
                up(mutualExclusion);
                up(empty);
            }
        }
    }
    wait(&waitStatus);
    return 0;
}
