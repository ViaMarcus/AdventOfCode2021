## Advent of Code 2021
Kotlin and Excel lol.

Day 1 and 2 were super quick in Excel. I did not even bother to save them.

Day 3 and 4 were not too bad.

Day 5 was horrible to do in Excel. 
The files grew too large and is not included here, even if part one was done with it.
It was done a bit late, but not never.

Day 6 and 7 was done in Excel and not too bad.

On the eight day, he was very disappointed with Excel's ability to work with arrays 
and do string lookups and replacements. Part one was easy, but part two was not.
Here, I set up a basic kotlin project and completed it with the more capable Kotlin.

On the ninth day, the Excel hope lived again, but died shortly after starting part 2...
It was a helpful tool in thinking about the solution, however.

Day 10 was completed without much ado. Here it was interesting to compare my solution with my friend,
as we had quite different approaches to the problem.

On day 14, I had not yet done day 11-13 to completion, but was enticed to start by my friend.
I noticed quickly it was similar to the reproducing fish from day 6, and solved part 2 in a similar way, 
although not in Excel, after doing part 1 the naive way.

Day 15 was a fun one, ending up not flattening my recursion but instead going to ever deeper depths by using Kotlin's
DeepRecursionFunction.

Day 16 was much fun, lots of lovely recursion and nesting. Had some issues with finding the padding zeroes but nothing
a try finally with prints can't get around.

Day 17 was interesting. Started to solve it by code until I realized it was easy to calculate the highest point based
on the highest velocity the probe was allowed to have after returning to y=0 and then doing a single large step.
But then part two needed the code I had started writing for part 1, so that was fine.

Day 18 was a nightmare to debug, forcing me to write some basic unit tests.

Day 19 is yet to be done.

Day 20 was quite simple, with some visual debugging.

Day 21 was quite similar to day 14 and 6, but with much more complexity in how the data structure split and incremented.

Day 22 took a lot of compute for me. And a lot of tweaking ranges with inclusive or exclusive. I found here that my
unit tests gave me a lot of confidence in my methods, and, while my tests initially did not find some edge cases 
correctly, after figuring that out, fixing the tests and adjusting my ranges a bit, I got the test data correct.
Then it was just a matter of running the program on the live data, for about 25 minutes... 
Not sure what makes it so slow, but I only needed to run it once, so that is good.