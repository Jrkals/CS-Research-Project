# CS-Research-Project
Textual Criticism research senior year
In order to run this there are several requirements:
  1) have a text file with the full paths of the input files
  2) the actual text of the input files must begin on the third line of the file
  3) the input files must be txt files and must be named with a 3 letter number followed by _copy e.g., 107_copy.txt, 444_copy.txt etc.
  4) don't use 100 for input I use that for the guess of the original text. Thus any number from 000 to 999 except 100 is ok. 
  5) for now (and I hope to change this one of the input files must be named 140_copy.txt
  6) for now make the longest input file the first i.e lowest in number (again this will change)
  7) the path to find all of this can be set in Macros.java. Just change the ROOT_DIRECTORY variable
The ouput is written to text files. This really only needs to be done once. In Macros.java you can set the various boolean variables to false or true. These act like pre-processor macros in C/C++ in the sense that they control which parts of code (in main) are run. 

UPGMA capability was also used. That works the same as the rest, have a file with the text paths and the texts must be named like above

I will push my variantList.txt as well as all .txt files and the UPGMAvariantList.txt as an example
