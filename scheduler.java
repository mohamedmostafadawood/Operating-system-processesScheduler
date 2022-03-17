import java.util.*;

public class test {

    public static String OutputFormat(String s){
        String curr = "";
        StringBuilder result = new StringBuilder();
        s+=".";
        List<String> ar = new ArrayList<String>();
        Collections.addAll(ar, s.split(","));

        ar.add("meows");
        curr =ar.get(0);
        int cnt =0;
        for(int i=1;i<ar.size()-1;i++){
            String next = ar.get(i);
            if(!curr.equals(next)){
                result.append(curr).append("(").append(cnt + 1).append(")").append(",");
                cnt =0;
            }
            else{
                cnt++;
            }
            curr = next;
        }

        if(!result.isEmpty())
            result.deleteCharAt(result.length()-1);

        return result.toString();
    }


    public static String Scheduler_FCFS(String input){
        StringBuilder result= new StringBuilder();
        List<String> run_time_list=process_run_time_split(input);
        List<String> processes_names_list = process_names_split(input);
        ArrayList<String> arrival = process_arriving_time_split(input);
        for (int i=0 ; i < processes_names_list.size() -1;i++) {
            String time_needed_tmp=run_time_list.get(i);
                result.append(processes_names_list.get(i)).append(String.format("(%s)", time_needed_tmp)).append(",");
        }
        result.append(processes_names_list.get(processes_names_list.size()-1)).append
                (String.format("(%s)", run_time_list.get(processes_names_list.size()-1)));
        return result.toString();
    }

    public static String Scheduler_RR(String input){
        Queue<String>Q = new LinkedList<>();
        HashMap<String,Integer> remTime = new HashMap<>();
        HashMap<String,Integer> Count = new HashMap<>();
        StringBuilder result = new StringBuilder();
        ArrayList<String> process_names = process_names_split(input);
        ArrayList<String> arrival_time = process_arriving_time_split(input);
        ArrayList<String> process_exec_time = process_run_time_split(input);

        int size = process_names.size();
        int totExec = 0;
        for(int i=0;i<size;i++){
            remTime.put(process_names.get(i),Integer.parseInt(process_exec_time.get(i)));
            totExec += Integer.parseInt(process_exec_time.get(i));
        }
        if(process_names.size()!=0) {
            String start = process_names.get(0);
            Q.add(start);
            Count.put(start, Math.min(2, remTime.get(start)));
        }

        for(int i=0;i<totExec;i++){
            // Add Processes that enters in this second
            for(int j=0;j<size;j++){
                if(Integer.parseInt(arrival_time.get(j)) == i
                        && !(i==0 && Objects.equals(Q.peek(), process_names.get(j)))) {
                    // if it's the first one then it's already added
                        Q.add(process_names.get(j));
                    }
            }

            String Process = Q.peek();
            // Round of Process ended
            if(Count.containsKey(Process) && Count.get(Process) == 0){
                // if it's finished just remove it
                if(remTime.get(Process) ==0){
                    Q.poll();
                }
                else{
                    // if not , add it to the back of the queue
                    Q.add(Q.poll());
                }
                // get new process and init it's counter
                if(!Q.isEmpty()) {
                    Process = Q.peek();
                    Count.put(Process, Math.min(2, remTime.get(Process)));
                }
            }

                // add Process to result and decrement cnt , remTime
            result.append(Process);
            result.append(",");
            remTime.put(Process,remTime.get(Process)-1);
            Count.put(Process,Count.get(Process)-1);

        }

    return OutputFormat(result.toString());
    //return result.toString();
        // AABBACCBBDDCCEEBBDDD
    }

    public static  ArrayList<String> process_names_split(String S){
    String [] parts = S.split(";");
    String processes_part = parts[0];
    return new ArrayList<String>(Arrays.asList(processes_part.split(",")));
    /// return processes_names_list;

    }


    public static  ArrayList<String> process_arriving_time_split(String S){
        String [] parts = S.split(";");
        String processes_part = parts[1];

        return new ArrayList<String>(Arrays.asList(processes_part.split(",")));
        ///return (ArrayList<String>) processes_arriving_time_list;

    }
    public static  ArrayList<String> process_run_time_split(String S){
        String [] parts = S.split(";");
        String processes_part = parts[2];

        return new ArrayList<String>(Arrays.asList(processes_part.split(",")));
        ///return (ArrayList<String>) processes_run_time_list;

    }


    public  static String Scheduler_SJF(String input){

        StringBuilder result= new StringBuilder();
        List<String> arriving_time_list =process_arriving_time_split(input);
        List<String> run_time_list=process_run_time_split(input);
        List<String> processes_names_list = process_names_split(input);
        List<String> processes_names_list_tmp = new ArrayList<String>();
        List<String> run_time_list_tmp = new ArrayList<String>();

        int consumed_time =0 ;


        while(!processes_names_list.isEmpty()){
            int j=0;
            while (!(arriving_time_list.isEmpty())
                    && Integer.parseInt(arriving_time_list.get(j)) <= consumed_time){
            processes_names_list_tmp.add(processes_names_list.get(j));
            run_time_list_tmp.add(run_time_list.get(j));
            processes_names_list.remove(j);
            run_time_list.remove(j);
            arriving_time_list.remove(j);
            }

            while(!processes_names_list_tmp.isEmpty()) {

              //String time_needed_tmp=run_time_list_tmp.get(i);
                //int min_run_time=0;
                int min_run_time_index=0;
                int min=Integer.parseInt(run_time_list_tmp.get(0));
                // Checker for the min
                for(int i=0; i<processes_names_list_tmp.size() ; i++){
                    if(Integer.parseInt(run_time_list_tmp.get(i)) < min){
                        min=Integer.parseInt(run_time_list_tmp.get(i));
                        min_run_time_index=i;
                    }
                }

               if(processes_names_list.isEmpty()&&processes_names_list_tmp.size()==1){
                    result.append(processes_names_list_tmp.get(min_run_time_index)).append(String.format("(%s)"
                            , run_time_list_tmp.get(min_run_time_index)));
                }
                else {
                    result.append(processes_names_list_tmp.get(min_run_time_index)).append
                            (String.format("(%s)", run_time_list_tmp.get(min_run_time_index))).append(",");
                }
                consumed_time+= Integer.parseInt(run_time_list_tmp.get(min_run_time_index)) ;
            processes_names_list_tmp.remove(min_run_time_index);
            run_time_list_tmp.remove(min_run_time_index);
            //System.out.println(result);
            }
            //System.out.println("Hello");

        }
return result.toString();
    }

    public static void main (String[]args){
//        String s= "A,B,C,D,E;0,2,4,5,8;3,6,4,5,2";
//        //String res = Schedule,r_FCFS(s);
//        String result= Scheduler_SJF(s);
//       System.out.println(result);
//        String test = "A,B,C,D,E;0,2,4,5,8;3,6,4,5,2";
//        System.out.println(test);
//        System.out.println(Scheduler_RR(test));
        Scanner sc = new Scanner(System.in);
        String in = sc.nextLine();
        System.out.println("Scheduler_FCFS(input) = \"" +Scheduler_FCFS(in) +"\"");
        System.out.println("Scheduler_SJF(input) = \"" +Scheduler_SJF(in) +"\"");
        System.out.println("Scheduler_RR(input) = \"" +Scheduler_RR(in) +"\"");

    }
}
