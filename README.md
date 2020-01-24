Goal:
- Create an API server which can analyse the content of big xml files
- A distributable docker container running the server (Bonus)

Assumptions:
- The XML content is ordered by Creation Date.
- The node has enough storage to store the files.

How to run the application:

$ docker run -p 8080:8080 diegogusava/merapar-demo

It is possible to connect VisualVM to the docker application, I exposed the port 9010.
