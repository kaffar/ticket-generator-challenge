# Ticket Generator Challenge

**Short description of the solution:**

* It is written as a Maven project with Java 11 and tests in Spock
* The recommended approach to execute the app - invoke Main class from your IDE (I didnt spend time to create a fat jar)
* About the algorithm: 
  - I must admit that I struggled a bit to create the algorithm that was not getting stuck when generating random tickets - I took several approaches but still one attempt out of e.g. 200 was stuck because of the order of how the tickets were generated
  - I decided to change the approach and tackle a problem from different angle. Instead of generating randomly the tickets and validating them afterwards or during the process of creation, I generated initial valid tickets at the start (full hardcoded tickets in the code) and then started process of swapping elements/numbers between tickets (the elements are swapped only when preelimiary check confirms that the tickets will be valid after swap).
  - The obvious advantage of this approach is that at every stage of generating the tickets - tickets are valid and complete
  - The approach that was taken still could be enhanced in terms of performance
  - I noticed additional benefit of taken apprach - the number of swaps between the tickets is configurable and the speed of the algorithm can be configurable as well thanks to that
