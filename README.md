android-inventory
=================

This is a simple Android implementation allowing the tracking of the number of Droid Devices within an organisation.

Was completed over a weekend in October 2014.

I developed this primarily to see the effects of both the Amazon and Google Bouncers. Interestingly there wasn't
much interesting from Google [who probably have some sort of photon shields running :)] however Amazon tests showed 
quite a few devices. These simply were registered along with the hardware details on the server-side MySQL.

Basically sends using RESTful services and JSON the hardware architecture details of an Android Device to a MySQL 
Database running on a Cloud LAMP Server (CentOS 6/Apache/MySQL/PHP), the PHP front-end processes and screens the 
JSON and simply inserts it into the MySQL database (same method in the opposite direction populating the device
with the HW updating).

A CustomListView displays the results. Although SQLite is used along with a Content Provider/Resolver, a contract
as well as a Cursor Loader these are somewhat overkill and a simple Array/ArrayAdapter would have handled everything.

Please note that you will need to alter the configuration to point to your server as well as write some small 
server-side code to be able to see the software functioning.

Uses org.json and org.apache.http client libraries.

ed@ryer.ru

