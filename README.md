# groot-inspired-task
A small example on how to use Kotlin channels and coroutines.
```bash
Usage: groot-inspired-task [OPTIONS] COMMAND [ARGS]...

  The input file must contain real numbers, each on a separate line. The
  output file will be created in a temporary directory and is not of interest.
  The essence of the task is to minimize execution time.

Options:
  -h, --help  Show this message and exit

Commands:
  fast     Really fast implementation, but possibly useless in common cases
  furious  The classic approach to handling channels
```

```bash
Usage: groot-inspired-task fast [OPTIONS]

  Really fast implementation, but possibly useless in common cases

Options:
  -i, --input TEXT    Input file path (TXT)
  -c, --capacity INT  The channel capacity. By default is 50000
  -h, --help          Show this message and exit
```

```bash
Usage: groot-inspired-task furious [OPTIONS]

  The classic approach to handling channels

Options:
  -i, --input TEXT    Input file path (TXT)
  -w, --workers INT   The number of channel consumers (the same for all
                      channels). By default is 50000
  -c, --capacity INT  Channels capacity. By default is 50000
  -h, --help          Show this message and exit
```

As mentioned above, the input file must contain real numbers, each on separate line. The application reads it and calls for each number a function that multiplies it by 2 and sleeps for 0.3 seconds (simulation of time consuming actions e.g. HTTP request). The purpose of the application is to process the file as quickly as possible. One approach is to use channel to communicate between coroutines. The output file with calculated results will be created in a temporary directory with the name prefix "groot-inspired-task-".

So, run one of the implementations:
```bash
$ groot-inspired-task fast -i /tmp/input.txt
```
```bash
$ groot-inspired-task furious -i /tmp/input.txt
```

You can generate an input file of the required size using this perl oneliner.
```perl
$ perl -E "say rand 1 for (1 .. 15000000)" > input.txt
```
