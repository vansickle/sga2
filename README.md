# sga2
Approximate brute-force approach. It doesn't guarantee to find the best path, but one of the best ones.
Firstly, it founds the path with minimum radiation, then it restricts to go the same way in the middle of the discovered path
and tries to find it again recursively.
It explores some paths and choose the best path according to these requirements: 
path should be <= 1290 and have the minimum possible radiation.

Searching shortest path algorithm: Dijkstra.

The best path -> Length : 1084 Radiation : 87890104
