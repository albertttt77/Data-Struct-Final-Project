import java.io.*;
import java.util.*;
/* ResourceManagement
 *
 * Stores the information needed to decide which items to purchase for the given budget and departments
 */
public class ResourceManagement
{
  private PriorityQueue<Department> departmentPQ; /* priority queue of departments */
  private Double remainingBudget;                 /* the budget left after purchases are made (should be 0 after the constructor runs) */
  private Double budget;                          /* the total budget allocated */
  
  /* TO BE COMPLETED BY YOU
   * Fill in your name in the function below
   */  
  public static void printName( )
  {
    /* TODO : Fill in your name */
    System.out.println("This solution was completed by:");
    System.out.println("Colby Christy");
    System.out.println("<student name #2 (if no partner write \"N/A\")>");
  }

  /* Constructor for a ResourceManagement object
   * TODO
   * Simulates the algorithm from the pdf to determine what items are purchased
   * for the given budget and department item lists.
   */
  public ResourceManagement( String fileNames[], Double budget )
  {
    this.budget = budget;
    this.remainingBudget = this.budget;
    this.departmentPQ = new PriorityQueue<>();
    for(String fileName : fileNames) {
      /* Create a department for each file listed in fileNames */
      File file = new File(fileName);
      Department department = new Department(file.toString());
      departmentPQ.add(department);
    }
    System.out.println("--------------BOUGHT!-------------");
    while(remainingBudget > 0.0){
      Department department = departmentPQ.poll();

      while(department.itemsDesired.peek() != null && department.itemsDesired.peek().price > remainingBudget){
        Item item = department.itemsDesired.remove();
        department.itemsRemoved.add(item);
        // System.out.println("Item Removed OG: " + item.name);
        
      }
      if(department.itemsDesired.peek() == null){
        Item scholarship = new Item("Scholarship", ((1000.0 < remainingBudget) ? 1000.0 : remainingBudget));
        department.itemsDesired.add(scholarship);
        //System.err.println(department.name + " given " + department.itemsDesired.peek().name);
        
      }
      else{
        
        if(department.itemsDesired.peek().price <= remainingBudget){
          Item item = department.itemsDesired.remove();
          department.itemsReceived.add(item);
          remainingBudget -= item.price;
          department.priority += item.price;
          String price = String.format("$%.2f", item.price );
          System.out.printf("Department of %-30s- %-30s- %30s\n", department.name, item.name, price );
          //System.out.println("Remaining Budget" + remainingBudget);
        }
        
      }
      departmentPQ.add(department);
    }
    // System.out.println(departmentPQ.size());
    Department department;
    for(int i = 0; i < departmentPQ.size();i++){
      department = departmentPQ.poll();
      department.priority = 1000000.0+i;
      Item item;
      // System.out.println(department.toString());
      while((item = department.itemsDesired.poll()) != null){
        department.itemsRemoved.add(item);
        // System.out.println("Item Removed: " + item.name);
      }
      departmentPQ.add(department);
    }

    // While the remaining budget is > 0 do the following: 
    // – Remove and save the front department of the priority queue. 
    // – While this departments have items, they desire and the price of the next item desired 
    // by this department is > the remaining budget, move that item to the department’s 
    // itemsRemoved list. 
    // – If there are no desired items in this department’s itemsDesired list, then they are given 
    // a scholarship equal to $1000 or the remaining the budget (whichever is smaller). 
    // – Else, the next desired item is purchased for this department. 
    // – In either case, remember to: 
    // * Update the priority of the department 
    // * Add the item to their list of itemsReceived list 
    // * Add this department back to the priority queue 
    // * Deduct the amount spent from the remaining budget 
    // * Print that this item was purchased (see Section 4 for more details) 

      /* Simulate the algorithm for picking the items to purchase */
      /* Be sure to print the items out as you purchase them */
      /* Here's the part of the code I used for printing prices as items */
      //String price = String.format("$%.2f", /*Item's price*/ );
      //System.out.printf("Department of %-30s- %-30s- %30s\n", /*Department's name*/, /*Item's name*/, price );
    
    
  } 

  /* printSummary
   * TODO
   * Print a summary of what each department received and did not receive.
   * Be sure to also print remaining items in each itemsDesired Queue.
   */      
  public void printSummary(  ){
    System.err.println("\n\n\n");
    while(departmentPQ.peek() != null){
      Department department = departmentPQ.poll();
      //System.out.println(department.toString());
      System.out.println("\n\nDepartment of " + department.name);
      department.priority = 0.0;
      for(int i = 0; i < department.itemsReceived.size();i++){
        Item item = department.itemsReceived.poll();
        department.priority += item.price;
        department.itemsReceived.add(item);
      } 

      String totalSpent = String.format("$%.2f", department.priority);
      System.out.println("Total Spent " + totalSpent);
      String percentage = String.format("%.2f", (department.priority/this.budget*100));
      System.out.println("Percent of Budget " + percentage + "%");
      System.out.println("------------------------------");
      if(department.itemsReceived.peek() != null){
      System.out.println("ITEMS RECIEVED");
      }
      while(department.itemsReceived.peek() != null){
        Item item = department.itemsReceived.remove();
        String price = String.format("$%.2f", item.price );
        System.out.printf("%-30s - %30s\n", item.name, price);
      }


      if(department.itemsRemoved.peek() != null){
        System.out.println("\nITEMS NOT RECIEVED");
        }
      while(department.itemsRemoved.peek() != null){
        Item item = department.itemsRemoved.remove();
        String price = String.format("$%.2f", item.price );
        System.out.printf("%-30s - %30s\n", item.name, price);
      }
    }
    /* Here's the part of the code I used for printing prices */
    //String price = String.format("$%.2f", /*Item's price*/ );
    //System.out.printf("%-30s - %30s\n", /*Item's name*/, price );
  }   
}

/* Department
 *
 * Stores the information associated with a Department at the university
 */
class Department implements Comparable<Department>
{
  String name;                /* name of this department */
  Double priority;            /* total money spent on this department */
  Queue<Item> itemsDesired;   /* list of items this department wants */
  Queue<Item> itemsReceived;  /* list of items this department received */
  Queue<Item> itemsRemoved;   /* list of items that were skipped because they exceeded the remaining budget */

  /* TODO
   * Constructor to build a Department from the information in the given fileName
   */
  public Department( String fileName ){
    File file = new File(fileName);
    
    try (Scanner input = new Scanner(file)){
      this.name = input.next();
      this.priority = 0.0;
      this.itemsDesired = new LinkedList<>();
      this.itemsReceived = new LinkedList<>();
      this.itemsRemoved = new LinkedList<>();

      while(input.nextLine() != null){
        String itemName = input.next();
        Double itemPrice = input.nextDouble();
        Item item = new Item(itemName,itemPrice);
        // System.out.print(itemName);
        //System.out.println(item.name + item.price);
        itemsDesired.add(item);
        
        // System.out.println("peek :" + itemsDesired);
      }
    }catch(Exception e){}

    /* Open the fileName, create items based on the contents, and add those items to itemsDesired */
  }
  
  /*
   * Compares the data in the given Department to the data in this Department
   * Returns -1 if this Department comes first
   * Returns 0 if these Departments have equal priority
   * Returns 1 if the given Department comes first
   *
   * This function is to ensure the departments are sorted by the priority when put in the priority queue 
   */
  public int compareTo( Department dept ){
    return this.priority.compareTo( dept.priority );
  }

  public boolean equals( Department dept ){
    return this.name.compareTo( dept.name ) == 0;
  }

  @Override 
  @SuppressWarnings("unchecked") //Suppresses warning for cast
  public boolean equals(Object aThat) {
    if (this == aThat) //Shortcut the future comparisons if the locations in memory are the same
      return true;
    if (!(aThat instanceof Department))
      return false;
    Department that = (Department)aThat;
    return this.equals( that ); //Use above equals method
  }
  
  @Override
  public int hashCode() {
    return name.hashCode(); /* use the hashCode for data stored in this name */
  }

  /* Debugging tool
   * Converts this Department to a string
   */	
  @Override
  public String toString() {
    return "NAME: " + name + "\nPRIORITY: " + priority + "\nDESIRED: " + itemsDesired + "\nRECEIVED " + itemsReceived + "\nREMOVED " + itemsRemoved + "\n";
  }
}

/* Item
 *
 * Stores the information associated with an Item which is desired by a Department
 */
class Item
{
  String name;    /* name of this item */
  Double price;   /* price of this item */

  /*
   * Constructor to build a Item
   */
  public Item( String name, Double price ){
    this.name = name;
    this.price = price;
  }

  /* Debugging tool
   * Converts this Item to a string
   */		
  @Override
  public String toString() {
    return "{ " + name + ", " + price + " }";
  }
}