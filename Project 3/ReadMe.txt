Austin Pilz
ANP147
Project 3


My VMSIM class is used as an interface to cleanly execute the page replacement algorithims found in my MemorySimulator class. I have included
three files - VMSIM which is the interface and command line executable, MemorySimulator which contains the page replacement algorithms and
PageTableEntry which is my PTE object.

To compile: javac vmsim.java

To run: java vmsim -n <numPages> -a <opt|clock|aging|work> -r <refreshRate (optional)> -t <tau (optional)> <traceFile>

If no refresh rate or tau are supplied to the MemorySimulator class, they will refault to a predefined value which was concluded to be
my optimal value as described in my write up.

I have included my excel findings which are the data I used to describe my conclusions in my write up DOC file.