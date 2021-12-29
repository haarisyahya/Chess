# Chess
A chess game made in Java using alpha-beta pruning. 
The game has a GUI and can be ran using the Interface class.
The associated program was written completely in Java and
aims to execute a fully-functional chess program that can be
played either as two human players, or against an artificial
intelligence. The program aims to implement an AI by utilising
alpha-beta pruning which will determine the AI’s moves. Since
chess is a fairly complex game in that it allows for a multitude
of moves from any given position, I have allowed the user
to select either two ply or four ply for the AI. Whilst not too
deep, these plies will allow for the AI to execute each move
within a reasonable time frame. Naturally, selecting a higher
ply will yield a more challenging AI as it looks deeper into
the benefits and consequences of each move

To execute the program, launch the associated
program in any java-supporting IDE and run the
GUI class. Please ensure that the following libraries
are installed or supported by the IDE in use:
javax.swing.∗, java.swing,plaf.BoarderUIResource, java.awt∗,
java.awt.event.ActionEvent, java.awt.event.ActionLisstener,
java.util.∗, java.util.List. When the class is run, this will then
present the interface shown in Fig 1, which will allow for
the game-mode and difficulty selection. Whilst the standard
chess-setup is the default, if a player so chose to, they could
alter the setup manually in the chess board class, which
would be supported.

Here is an image of the interface:

![image](https://user-images.githubusercontent.com/80646420/147707211-48fa815b-b6b5-49e1-aab8-0ba84a8b1857.png)

Here is the game:

![image](https://user-images.githubusercontent.com/80646420/147707206-d7eb4f28-74b7-4c1f-9078-3d1669596b52.png)
