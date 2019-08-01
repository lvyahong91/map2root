package testThreadLocal;

public class SequenceC implements Sequence{
    private static MyThreadLocal<Integer> numContainer=new MyThreadLocal<Integer>(){
        @Override
        protected Integer initValue() {
            return 0;
        }
    };

    @Override
    public int getNumber() {
        numContainer.set(numContainer.get()+1);
        return numContainer.get();
    }

    public static void main(String[] args){
        Sequence sequence=new SequenceC();

        ClientThread thread1=new ClientThread(sequence);
        ClientThread thread2=new ClientThread(sequence);
        ClientThread thread3=new ClientThread(sequence);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
