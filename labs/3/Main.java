import mpi.MPI;
import mpi.*;

import java.util.*;

public class Main {
  public static void main(String[] args) throws Exception {
    int TAG = 0;

    MPI.Init(args);

    int rank = MPI.COMM_WORLD.Rank();
    int size = MPI.COMM_WORLD.Size();

    if (rank > 2) {
      Random rand = new Random();

      int[] s = new int[] { rand.nextInt(10) + 1 };

      if (rank % 2 == 0) {
        MPI.COMM_WORLD.Isend(s, 0, s.length, MPI.INT, 2, TAG);
      } else {
        MPI.COMM_WORLD.Isend(s, 0, s.length, MPI.INT, 1, TAG);
      }
    }

    if (rank == 2) {
      int[] s = new int[1];
      int[] arr = new int[(size - 3) / 2];

      Request[] arrRecv = new Request[(size - 3) / 2];

      int j = 0;

      for (int i = 4; i < size; i += 2){
        Request request = MPI.COMM_WORLD.Irecv(arr, j, s.length, MPI.INT, i, TAG);                

        arrRecv[j] = request;                

        j++;
      }

      Request.Waitall(arrRecv);

      Arrays.sort(arr);

      MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, 0, TAG);
    }

    if (rank == 1) {
      int[] s = new int[1];
      int[] arr = new int[(size - 3) / 2];

      Request[] arrRecv = new Request[(size - 3) / 2];

      int j = 0;

      for (int i = 3; i < size; i += 2){
        Request request = MPI.COMM_WORLD.Irecv(arr, j, s.length, MPI.INT, i, TAG);                

        arrRecv[j] = request;                

        j++;
      }

      Request.Waitall(arrRecv);

      Arrays.sort(arr);

      MPI.COMM_WORLD.Isend(arr, 0, arr.length, MPI.INT, 0, TAG);
    }

    if (rank == 0) {
      int[] s1 = new int[(size - 3) / 2];
      int[] s2 = new int[(size - 3) / 2];

      int[] arr = new int[size - 3];

      Request[] requests = new Request[2];

      requests[0] = MPI.COMM_WORLD.Irecv(arr, 0, (size - 3) / 2, MPI.INT, 1, TAG);
      requests[1] = MPI.COMM_WORLD.Irecv(arr, (size - 3) / 2, (size - 3) / 2, MPI.INT, 2, TAG);

      Request.Waitall(requests);

      Arrays.sort(arr);

      for (int a : arr) {
        System.out.print(String.format("%d ", a));
      }
    }
  }
}
