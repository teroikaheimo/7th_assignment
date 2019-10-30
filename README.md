# 7th_assignment

Go to https://loremflickr.com/

The service has a API where you can get a picture related to 1-n tags given to the query. 
For example https://loremflickr.com/json/g/320/240/cat/all gives you metadata for a picture tagged "cat". 
Metadata includes the url for the actual picture of the cat.

Construct an application where you have a smaller "toolbar" section dedicated for controls and larger section dedicated 
for presenting the data. The toolbar-section has darker background colour to distinct it from the data-section.

The toolbar-section has an EditText-view and a Button.

The data-section is filled with an initially empty ListView.

The Button and the EditText are not written in the layout file, but instead are added dynamically to the layout-view 
in the code as the program starts.

Upon pressing the button the program makes a query to loremflickr's json service where the tag for the query is read 
from the EditText-field. This means it should response with a metadata of a picture related to the given tag. Empty EditText-field will not do at all and whole program is terminated because of that.

In the ListView the name of the owner and the license of the picture is displayed on the same row with the picture. 
This means you need to make another query for the picture based on the metadata you get from the first query.

Pressing the button consecutive times with different tags should add more rows to the list, with correct owners and 
licenses next to the pictures.
