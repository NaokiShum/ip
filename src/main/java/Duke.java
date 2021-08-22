import java.io.FileNotFoundException;
import java.util.Scanner;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Duke {

    public static void main(String[] args) throws FileNotFoundException {
        String line = "____________________________________________________________\n";
        Ui ui = new Ui();
        ui.Start();
        boolean exit = false;
        Scanner sc = new Scanner(System.in);
        String filePath = "data/duke.txt";
        Storage save = new Storage(filePath);
        TaskList tasks = new TaskList(save.readFile(filePath));
        int count = tasks.size();

        while(sc.hasNext()){
            String command = sc.next();
            switch(command){
                case "bye":
                    ui.Bye();
                    exit = true;
                    break;
                case "list":
                    String list = "";
                    int listNum = 1;
                    for(int i = 0;i<tasks.size();i++){
                        list += listNum + "." + tasks.get(i) +"\n";
                        listNum++;
                    }
                    System.out.println(line + list + line);
                    break;
                case "done":
                    int doneNum = sc.nextInt() - 1;
                    try{
                        tasks.get(doneNum).markAsDone();
                        ui.Done(tasks,doneNum);
                        break;
                    }catch (IndexOutOfBoundsException e){
                        ui.showDoneError();
                        break;
                    }
                case "delete":
                    int delNum = sc.nextInt()-1;
                    try{
                        Task delete = tasks.get(delNum);
                        count--;
                        ui.Delete(delete,count);
                        tasks.remove(delNum);
                        break;
                    }catch (IndexOutOfBoundsException e){
                        ui.showDeleteError();
                        break;
                    }
                case "todo":
                    try {
                        Task todo = new Todo(sc.nextLine().trim(), count);
                        tasks.add(todo);
                        count++;
                        ui.todo(todo,count);
                        break;
                    } catch (Exception e){
                        ui.showTodoError();
                        break;
                    }
                case "deadline":
                    try {
                        String[] deadlineArr = sc.nextLine().split("/by");
                        LocalDate d1 = LocalDate.parse(deadlineArr[1].trim());
                        Task deadline = new Deadline(deadlineArr[0].trim(),
                                d1.format(DateTimeFormatter.ofPattern("MMM dd YYYY")), count);
                        tasks.add(deadline);
                        count++;
                        ui.deadline(deadline,count);
                        break;
                    } catch (DateTimeParseException e){
                        ui.showDeadlineError1();
                        break;
                    } catch (Exception e){
                        ui.showDeadlineError2();
                        break;
                    }
                case "event":
                    try {
                        String[] eventArr = sc.nextLine().split("/at");
                        Task event = new Event(eventArr[0].trim(),eventArr[1].trim(), count);
                        tasks.add(event);
                        count++;
                        ui.event(event,count);
                        break;
                    } catch (Exception e){
                        ui.showEventError();
                        break;
                    }
                default:
                    ui.defaultError();
            }

            save.writeToFile(filePath,tasks);

            if(exit){
                break;
            }
        }
    }
}
