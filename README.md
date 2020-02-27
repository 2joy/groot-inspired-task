# groot-inspired-task
A small example on how to use Kotlin channels and coroutines.
```bash
$ groot-inspired-task -i input.txt
```

The input file must contain real numbers, each on sepparate line. The application reads it and calls for each number a function that multiplies it by 2 and sleeps for 0.3 seconds (simulation of time consuming actions e.g. HTTP request). The purpose of the application is to process the file as quickly as possible. One approach is to use channel to communicate between coroutines. The output file with calculated results will be created in a temporary directory with the name prefix "groot-inspired-task-".

You can generate an input file of the required size using this perl oneliner.
```perl
$ perl -E "say rand 1 for (1 .. 15000000)" > input.txt
```
