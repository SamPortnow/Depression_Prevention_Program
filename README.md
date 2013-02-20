Depression_Prevention_Program
=============================

Android app modeled from the Depression Prevention Course

The components of the app are:

1) Users keep a diary tracking what they did, how they felt, and what they were thinking

2) Users learn to replace negative thoughts with positive ones with the "negative thought destroyer game"

3) Users learn what activities make them feel good, and what activities make them feel bad with a graphical
representation of their daily diary


The App Itself
=============================

Two main components: daily diary and "negative thought destroyer game"

Daily diary:

Users enter their activities throughout the day, along with thoughts and current mood. 

Thoughts are tagged with a - or + for negative or positive thoughts. 

Negative Thought Destroyer Game

Similar to "Puzzle Bobble", but instead of bubbles, negative thought clouds will populate the screen, and 
"inside" each negative thought cloud (i.e., a gray cloud) will be the negative thoughts that the user inputed into
the daily diary. If the user has not entered any/not enough negative thoughts, a default array of negative thoughts
will be provided. The user will type into an edit text field at the bottom of the screen and click the "fire" 
button to destroy the negative thoughts. A positive thought cloud (i.e., a nice white cloud), will float from 
the edit text field to the negative thought cloud, when they touch, the negative thought cloud will blow up, 
and the positive thought cloud will take its place.

The game will be over when positive thoughts populate the screen, instead
of negative thoughts -- although I am open to other ideas about what will win the game. 



#TODO 

1) Sound effects. Lightning for "negative thought clouds", explosions for when they are destroyed, bird 
chirping for when the negative thought clouds are destroyed?

2) Decide what consitutes the end of the game. When all of the negative thoughts from the db are destroyed?
When there are only positive thoughts on the screen -- and no more room for negative thoughts? In order to do
this there would have to be a default string array of negative thoughts, and which negative thoughts would 
pop up would be random. I am leaning towards this option. 

3) Keep track of points (and develop a scoring system). 
