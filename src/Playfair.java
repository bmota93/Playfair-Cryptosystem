/**
 * <pre>
 * 
 * Author:
 *  	Brandon Mota 
 *  
 *  
 * Description: 
 * 		This program encrypts and decrypts a message from an input file
 * 		with the Playfair cryptosystem. Use the Java command from a command-line-terminal (e.g "java Playfair <function> <file>")
 * 
 * 
 * </pre>
 */

import java.util.*;
import java.io.*;

public class Playfair
{
		
	private char[][] playfairmatrix;
	private File userfile;
	private ArrayList<Character> comparearray , finalarray;
	
	/**
	 * <pre>
	 * Description: 
	 * This is the constructor.
	 * Pre:
	 * none.
	 * Post:
	 * none.
	 * </pre>
	 */
	public void Playfair()
	{
		
	}
	
	/**
	 * <pre>
	 * Description: 
	 * This creates the key matrix neccessary for
	 * encryption and decryption.
	 * Pre:
	 * The first line of the input file must be the key.
	 * Post:
	 * Key matrix is created.
	 * </pre>
	 */
	public void createkeymatrix(String key)
	{	
		playfairmatrix = new char[5][5];
		key = key.toUpperCase();
		key = key.replaceAll("[^A-Z]", ""); //replaces all lowercase characters with uppercase
		key = key.replace("J", "I"); // replace J with I
						
		String cipher = key + "ABCDEFGHIKLMNOPQRSTUVWXYZ";
								
		for (int i = 0; i < cipher.length(); i++)
		{
			boolean repeatchar = false;
			boolean usedchar = false;
			
			for (int j = 0; j < 5; j++)
			{
				for (int k = 0; k < 5; k++)
				{
					if (playfairmatrix[j][k] == (cipher.charAt(i)))
					{
						repeatchar = true;						
					}
					else if ((playfairmatrix[j][k] == ('\0')) && !repeatchar && !usedchar)
					{						
						playfairmatrix[j][k] = cipher.charAt(i);
						usedchar = true;
					}
				}
			}
		}
		
		
		System.out.println("Here is the key matrix:\n");
		for (int y = 0; y < 5; y++)
		{
			for (int x = 0; x < 5; x++)
			{
				System.out.print(playfairmatrix[y][x]);
			}
			System.out.println();
		}
		
	}	
	
	/**
	 * <pre>
	 * Description: 
	 * This encrypts the message from the
	 * given input file, msgin.txt.
	 * Pre:
	 * none.
	 * Post:
	 * The cleartext is encrypted and output to cipout.txt.
	 * </pre>
	 */
	public void encrypt(File file) throws IOException
	{
		comparearray = new ArrayList<Character>();   
		finalarray = new ArrayList<Character>();
        char[] setarraysize = new char [320];
        char[] fileinputarray;
        int size = 0;
        
		Scanner scan;		
		scan = new Scanner(file);
		String key = scan.nextLine();
		System.out.println("Here is the key: " + key);			
		createkeymatrix(key);
		BufferedWriter writer = new BufferedWriter(new FileWriter("cipout.txt"));
		
		while (scan.hasNextLine())
		{			
			String sb = scan.nextLine();
			sb = sb.toUpperCase().replaceAll("\\s+","");
			for (int i = 0; i < sb.length(); i++)
			{				
				for (int j = 0; j < setarraysize.length; j++)
				{
						if (setarraysize[j] == '\u0000')
						{
							setarraysize[j] = sb.charAt(i);							
							break;
						}
				}
			}						
		}
		for (int i = 0; i < setarraysize.length; i++)
		{	
			if (setarraysize[i] == '\u0000')
			{
				size = i;
				break;
			}
		}	
		fileinputarray = new char[size];
		
		for (int i = 0; i < fileinputarray.length; i++)
		{
			fileinputarray[i] = setarraysize[i];
		}
		
			for (int i = 0; i < fileinputarray.length; i++)
			{   
	            char ch= fileinputarray[i];   
	            if(ch == 'J')
	            {
	            	ch = 'I'; //makes sure that all J's are replaced with I's
	            }
	            comparearray.add(ch);   
	        } 
			boolean addX = false;   
	        for (int i = 0; i < (comparearray.size())/2; i++)
	        {   
	            if(addX)
	            {   
	                i=0;   
	                addX=false;   
	            }    
	            if(comparearray.get(i*2).equals(comparearray.get(i*2+1)))
	            {   
	                addX = true;   
	                comparearray.add(i*2+1,'X');   
	            } 
	        }
	        if((comparearray.size() % 2) != 0)
	        {
	        	comparearray.add(comparearray.size(), 'X');
	        }
	        
	        System.out.println("\nEncrypting the cleartext: ");
	        for (int i = 0; i < comparearray.size(); i++)
	        {	        	
	        	System.out.print((Character)comparearray.get(i));
	        }
	        
	        int r1=5,l1=5;   
	        int r2 =5, l2=5;   //set equal to 5 to ensure a 5x5 matrix rules are followed
	           
	        for (int i = 0; i < comparearray.size()/2; i++) //only half the comparing array since two char compare
	        {   
	            for (int j = 0; j < 5; j++)
	            {   
	                for (int j2 = 0; j2 < 5; j2++)
	                {   
	                    if(((Character)comparearray.get(i*2)).charValue()==playfairmatrix[j][j2]) //first letter to compare
	                    {   
	                        r1=j;   //tries to find char in key matrix  
	                        l1=j2;   
	                           
	                    }   
	                    if(((Character)comparearray.get(i*2+1)).charValue()==playfairmatrix[j][j2]) //second letter to compare
	                    {   
	                        r2 = j;   
	                        l2 = j2;
	                    }   
	                }   
	            }   
	            if(r1==r2) //if rows match, follow rule take right char
	            {   
	            	finalarray.add(i*2,playfairmatrix[r1][(l1+1)%5]);
	            	finalarray.add(i*2+1,playfairmatrix[r2][(l2+1)%5]);   
	            }
	            else if(l1==l2) //if line match, follow rule take down char
	            {   
	            	finalarray.add(i*2,playfairmatrix[(r1+1)%5][l1]);   
	            	finalarray.add(i*2+1,playfairmatrix[(r2+1)%5][l2]);   
	            }
	            else //rectangle shape, so take corners
	            {   
	            	finalarray.add(i*2,playfairmatrix[r1][l2]);   
	            	finalarray.add(i*2+1,playfairmatrix[r2][l1]);   
	            }          
	        }
	        
	        boolean addspace = false;
	        for (int i = 0; i < finalarray.size(); i++)
	        {   
	        	if (addspace == true)
	            {  
	        		if (i % 32 == 0) //next line after 32 characters
	        		{
	        			writer.newLine();
	        		}
	        		else if (i % 4 == 0) //creates space every 4 characters
	            	{
	            		writer.write(" ");
	            	}	            	
	            }
	            addspace = true;
	        	writer.write(((Character) finalarray.get(i)).charValue());         
	        } 
	        writer.close();
	        System.out.println("\n\nDone. Ciphertext located in file: cipout.txt");
	}	 
	
	/**
	 * <pre>
	 * Description: 
	 * This decrypts the message located in the file cipin.txt 
	 * Pre:
	 * none.
	 * Post:
	 * The ciphertext is decrypted and output to msgout.txt.
	 * </pre>
	 */
	public void decrypt(File file) throws IOException
	{
		comparearray = new ArrayList<Character>();   
		finalarray = new ArrayList<Character>();
        char[] setarraysize = new char [320];
        char[] fileinputarray;
        int size = 0;
        
		Scanner scan;		
		scan = new Scanner(file);
		String key = scan.nextLine();
		System.out.println("Here is the key: " + key);			
		createkeymatrix(key);
		BufferedWriter writer = new BufferedWriter(new FileWriter("msgout.txt"));
		
		while (scan.hasNextLine())
		{			
			String sb = scan.nextLine();
			sb = sb.toUpperCase().replaceAll("\\s+","");
			for (int i = 0; i < sb.length(); i++)
			{				
				for (int j = 0; j < setarraysize.length; j++)
				{
						if (setarraysize[j] == '\u0000')
						{
							setarraysize[j] = sb.charAt(i);						
							break;
						}
				}
			}						
		}
		for (int i = 0; i < setarraysize.length; i++)
		{	
			if (setarraysize[i] == '\u0000')
			{
				size = i;
				break;
			}
		}	
		fileinputarray = new char[size];	
		
		for (int i = 0; i < fileinputarray.length; i++)
		{
			fileinputarray[i] = setarraysize[i];
		}	
			
		for (int i = 0; i < fileinputarray.length; i++)			
		{
			comparearray.add(fileinputarray[i]);     
		} 
			
			
			
		System.out.println("\nDecrypting the ciphertext: ");	        
		for (int i = 0; i < comparearray.size(); i++)	        
		{	        		        	
			System.out.print((Character)comparearray.get(i));	        
		}	        
	        
		int r1=5,l1=5; 	       
		int r2 =5, l2=5;  
	           
	        
		for (int i = 0; i < comparearray.size()/2; i++)	        
		{ 
			for (int j = 0; j < 5; j++)	           
			{   	                
				for (int j2 = 0; j2 < 5; j2++)	                
				{  	                    
					if(((Character)comparearray.get(i*2)).charValue()==playfairmatrix[j][j2])	                  
					{  	                    
						r1=j; 	                       
						l1=j2;                
					}  	                    
					if(((Character)comparearray.get(i*2+1)).charValue()==playfairmatrix[j][j2])	                    
					{  	                        
						r2 = j; 	                        
						l2 = j2; 	                    
					} 	
				}
			}   
	            
			if(r1==r2)
			{   	
				finalarray.add(i*2,playfairmatrix[r1][(l1+4)%5]); 
				finalarray.add(i*2+1,playfairmatrix[r2][(l2+4)%5]); 
			}   
			else if(l1==l2)
			{   
				finalarray.add(i*2,playfairmatrix[(r1+4)%5][l1]); 
				finalarray.add(i*2+1,playfairmatrix[(r2+4)%5][l2]); 
			}
			else
			{   
				finalarray.add(i*2,playfairmatrix[r1][l2]);  
				finalarray.add(i*2+1,playfairmatrix[r2][l1]); 
			} 
		}
		boolean addspace = false;
		for (int i = 0; i < finalarray.size() - 1; i++)
		{   
			if (addspace == true)
			{  
				if (i % 32 == 0)
				{
					writer.newLine();
				}
				else if (i % 4 == 0)
				{
					writer.write(" ");
				}	       
			}
			addspace = true;
			writer.write(((Character) finalarray.get(i)).charValue());
		}
		
		char comparex = (Character) finalarray.get(finalarray.size() - 1).charValue();
	        
		if (comparex != 'X') //checks last character is not X, assuming it was added due to odd number length
		{
			writer.write(((Character) finalarray.get(finalarray.size() - 1)).charValue()); //writes last character
		}	 
		writer.close();
		System.out.println("\n\nDone. Cleartext located in file: msgout.txt");
	}	
	
	/**
	 * <pre>
	 * Description: 
	 * This is the main method. This only accepts command line arguments.
	 * </pre>
	 */		
	public static void main(String[] args) throws IOException
	{		
		Playfair run = new Playfair();
		String function = null;
		
		for (String s: args) //accepts command line arguments
		{
            if (s.equalsIgnoreCase("enc") || s.equalsIgnoreCase("dec"))
            {
            	function = s;
            }
            else if (s.equalsIgnoreCase("msgin.txt") || s.equalsIgnoreCase("cipin.txt"))
            {
            	if (function.equalsIgnoreCase("enc"))
            	{
            		run.userfile = new File(s);
            		System.out.println("Encrypting " + run.userfile+ "...\n");
                	run.encrypt(run.userfile);
            	}
            	else
            	{
            		run.userfile = new File(s);
            		System.out.println("Decrypting " + run.userfile + "...\n");
                	run.decrypt(run.userfile);
            	}
            }            
            else
            {
            	System.out.println("Unknown Command: " + s);
            	System.out.println("Please enter the command line in the correct format:");
            	System.out.println("java Playfair <function> <file>");
            	System.out.println("Only 'msgin.txt' or 'cipin.txt' are accepted for file names.");
            }
        }
	}

} //end of program
