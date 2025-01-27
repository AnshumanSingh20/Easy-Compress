import java.io.*;
import java.util.PriorityQueue;

public class HuffmanTree implements Serializable {
   public final static int SIZE = 256;
   public final static int BYTE_SIZE = 128;
   protected HuffmanNode<Integer> root;
   protected int[] marker;

   public HuffmanTree() {
      root = null;
      setMarker(new int[] {});
   }

   public HuffmanTree(String filename) {
      
      int[] freqTable = new int[SIZE];
      initializeArray(freqTable, 0);
      InputStream in = null;
      try {
         in = new FileInputStream(new File(filename));
         byte[] items = in.readAllBytes();
         for (int i = 0; i < items.length; i++) {
            freqTable[items[i] + BYTE_SIZE]++;
         }
         in.close();
      } catch (IOException ex) {
         return;
      }
      setMarker(freqTable);
      
      freqTable[marker[0]]++;
      createHuffmanTree(freqTable);
   }

   private static void initializeArray(int[] array, int num) {
      for (int i = 0; i < array.length; i++) {
         array[i] = num;
      }
   }

   private void setMarker(int[] freq) {
      if (freq.length == 0) {
         marker = new int[] { 0, 0 };
         return;
      }
      for (int i = 0; i < freq.length; i++) {
         if (freq[i] == 0) {
            marker = new int[] { i, 0 };
            return;
         }
      }
      
      int min = 0, minIndex = 0;
      while (freq[minIndex] == 0) {
         minIndex++;
      }
      min = freq[minIndex];
      for (int i = minIndex + 1; i < freq.length; i++) {
         if (freq[i] != 0 && min > freq[i]) {
            min = freq[i];
            minIndex = i;
         }
      }
      marker = new int[] { minIndex, min };
   }

   public HuffmanNode<Integer> createNewNode(Integer e, int w) {
      return new HuffmanNode<Integer>(e, w);
   }

   protected void setCodeTable(String[] table) {
      setCode(root, "", table);
   }

   private void setCode(HuffmanNode<Integer> node, String code, String[] table) {
      if (node == null) {
         return;
      }
      setCode(node.getLeft(), code + "0", table);
      if (node.getLeft() == null && node.getRight() == null) {
        
         table[node.getPayload()] = code;
      }
      setCode(node.getRight(), code + "1", table);
   }

   private void createHuffmanTree(int[] occurrence) {
      PriorityQueue<HuffmanNode<Integer>> queue = new PriorityQueue<>();
      for (int i = 0; i < SIZE; i++) {
         if (occurrence[i] != 0) {
            queue.offer(createNewNode(i, occurrence[i]));
         }
      }
      int size = queue.size();
      for (int i = 0; i < size - 1; i++) {
         HuffmanNode<Integer> n1 = queue.poll();
         HuffmanNode<Integer> n2 = queue.poll();
         
         HuffmanNode<Integer> parent = createNewNode(-1, n1.getWeight() + n2.getWeight());
         parent.setLeft(n1);
         parent.setRight(n2);
         
         queue.offer(parent);
      }
      
      root = queue.poll();
   }

   public void inorder(HuffmanNode<Integer> node, HuffmanNode<Integer> parent) {
      if (node == null && parent != null) {
         System.out.print("" + parent.getPayload() + "  ");
         return;
      }
      inorder(node.getLeft(), node);
      inorder(node.getRight(), node);
   }
   

}