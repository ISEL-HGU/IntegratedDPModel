package edu.handong.csee.isel.weka;

public class WorkerThread implements Runnable {
	  
    private String command;
    
    public WorkerThread(String s){
        this.command=s;
    }

    @Override
    public void run() { // 이 부분이 실제로 thread가 도는 부분인 듯 
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
//        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}