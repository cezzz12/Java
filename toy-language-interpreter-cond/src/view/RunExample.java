package view;

import controller.Controller;
import model.exceptions.MyException;

public class RunExample extends Command {
    private Controller ctr;

    public RunExample(String key, String desc, Controller ctr) {
        super(key, desc);
        this.ctr = ctr;
    }

    @Override
    public void execute() {
        try {

            ctr.allStep();
            System.out.println("Program execution completed successfully.");
        } catch (MyException e) {
            System.out.println("Execution error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e);
            e.printStackTrace();
        }
    }
}