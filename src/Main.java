import routing.DV;

public class Main {

    public static void main(String[] args) {
    	DV dv = new DV();
    	dv.update( "192.168.0.3", "192.168.0.2", 1);
    	dv.update( "192.168.0.4", "192.168.0.2", 1);
    	dv.update( "192.168.0.8", "192.168.0.5", 1);
    	System.out.println(new String(dv.getByteArray()));
    	System.out.println(new String(new DV(dv.getByteArray()).getByteArray()));
    }
}
