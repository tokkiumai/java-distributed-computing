/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpjexample1;
import mpi.*;
import java.util.*;
/**
 *
 * @author Tima
 */
public class MPJExample1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception {
        // TODO code application logic here
        int TAG = 0;
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int[] s = {0};
//        if(rank + 1 != size){
//            if(rank == 0){
//                MPI.COMM_WORLD.Send(s, 0, 1, MPI.INT, rank + 1, TAG);
//                System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+(rank+1));
//                MPI.COMM_WORLD.Recv(s, 0, 1, MPI.INT, 4, TAG);
//                System.out.println("result: "+Arrays.toString(s));
//            }
//            else {
//                MPI.COMM_WORLD.Recv(s, 0, 1, MPI.INT, rank - 1, TAG);
//                s[0] += rank;
//                System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+(rank + 1));
//                MPI.COMM_WORLD.Send(s, 0, 1, MPI.INT, rank + 1, TAG);
//            }
//        }
//        else{
//            MPI.COMM_WORLD.Recv(s, 0, 1, MPI.INT, rank - 1, TAG);
//            s[0] += rank;
//            System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+0);
//            MPI.COMM_WORLD.Send(s, 0, 1, MPI.INT, 0, TAG);
//        }
        
        if(rank + 1 != size){
            if(rank == 0){
                MPI.COMM_WORLD.Isend(s, 0, 1, MPI.INT, rank + 1, TAG);
                System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+(rank+1));
                Request request = MPI.COMM_WORLD.Irecv(s, 0, 1, MPI.INT, 4, TAG);                
                request.Wait();
                System.out.println("result: "+Arrays.toString(s));
            }
            else {
                Request request = MPI.COMM_WORLD.Irecv(s, 0, 1, MPI.INT, rank - 1, TAG);
                request.Wait();
                s[0] += rank;
                System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+(rank + 1));
                MPI.COMM_WORLD.Isend(s, 0, 1, MPI.INT, rank + 1, TAG);
            }
        }
        else{
            Request request = MPI.COMM_WORLD.Irecv(s, 0, 1, MPI.INT, rank - 1, TAG);
            request.Wait();
            s[0] += rank;
            System.out.println("from "+rank+" send "+Arrays.toString(s)+" to "+0);
            MPI.COMM_WORLD.Isend(s, 0, 1, MPI.INT, 0, TAG);
        }
        MPI.Finalize();
    }
    
}
