							Password Locker

Summary– 

In this digital age, individuals generate innumerable credentials pertaining to innumerable secured websites and applications that they deal with, day in & day out, to manage their work and living.  Nothing can be crucial than the safety and confidentiality of these data.  This calls for creating, maintaining and remembering as complex passwords as possible as part of the credentials to these secured websites and applications in service to a new digital age individual.   

Remembering number of credentials / passwords of this magnitude may prove insane to an individual.  Way around may be maintaining simple passwords and common passwords for all credentials.  But this approach can compromise with data security with serious consequences.  

Fortunately, the software can be the savior in this situation.  Password Locker is an initiative in that direction. I developed ‘Password Locker’ as a personal project of my own in Android mobile platform.  I am still enhancing it adding features like Dropbox cloud integration etc.

Password Locker is a configurable and scalable personal credential management native App for Android phones and tablets. It is built with Android Material Design principles, and complies with Android 5.0 (API level 21) and above. 

It offers the following capabilities in nutshell -
⎫	A simple data model to group and store Credentials (Accounts and Credentials)
⎫	Manage Accounts and Credentials (Create, Retrieve, Update, Delete from SQLITE database)
⎫	Send secured emails with Account details through a background thread (Async Task)
⎫	Track and Send taskbar notification for password change management
⎫	Download Credential data in Excel spreadsheet and store it in either device’s external storage or Dropbox cloud storage (Dropbox integration development is in progress) 
⎫	Retrieve and open saved excel sheet from device’s external storage or Dropbox cloud storage (Dropbox integration development is in progress) 
⎫	Load data from excel sheets in to the App.

Among other features, the App provides clean and easy-to-follow UIs with Material Design theme consisting of –
⎫	Activity transition animations (Common element scene transitions and customized activity transitions)
⎫	Translucent screens
⎫	User interactive Grid views and List views
⎫	Task bar Notification using RTC_WAKEUP Alarm Manager and BroadcastReceiver
⎫	Dynamic item search in List Views using Text Changed Listeners
⎫	Password validity checking Progress bar



How it works:

Account and Credential – 






Account management – 
An Account is a like a container for Credentials.  One Account can have many credentials associated to it.  The Credentials for one Account would remain logically partitioned from the Credentials of other Accounts.  Multiple Accounts can be created. Multiple Credentials, given by Subject names, can be created within an Account.

An Account can have two attributes -  
⎫	the View password (used to view Credentials within the Account)
⎫	the email Id – used  to receive Account login details through systematic email 

A Credential has the following three attributes –
•	Subject name
•	User Id
•	Password

Example – 
An Account can be defined like ‘Domestic’ or ‘official’.  
The ‘Domestic’ Account may contain many credentials given by the subject names, like ‘Credit Card’, Facebook etc.

The ‘official’ Account may contain many credentials given by the subject names, like ‘Bank’, ‘Timesheet at work’ etc.



Credential management Workflow model – 


https://github.com/svjt78/PasswordLocker/files/129978/PasswordLockerWorkflow.docx

Credential management and change notifications -
Credentials within a given Account can be created, updated or deleted.  
Change history of passwords, for the credentials, is tracked through storing information like ‘Last password change date’ and ‘Password validity period’.  

Credentials can be viewed in List views and searched.  Rules can be set around how passwords should be stored and managed for Credentials. Rules can be set for the app to track if the passwords should be changed at regular intervals as configured, or not.  If the passwords should be changed at regular intervals then the App automatically tracks due or overdue passwords and sends Taskbar Notifications to the App user for password change.


Data Externalization and Cloud hosting -
The App allows downloading of all Credential data pertaining to an Account in Excel spreadsheet, and save the spreadsheet in external storage of the device.  
It will also allow saving the spreadsheet in Dropbox Cloud storage as part of an ongoing enhancement.

The App also allows uploading data from existing Excel spreadsheets in a specific format.  The data uploaded from the spreadsheet gets loaded in the Sqlite database of the App, and then can be viewed and managed through the App.  The spreadsheet can be loaded either from external storage of the device or from Dropbox cloud storage.

Following information are downloaded and stored in the spreadsheet at Credential level for a given account –

Account name
Subject name
User Id
Password
Last password change date 
Password validity period

(Integration to Dropbox is still under development)

Source code -

https://github.com/svjt78/PasswordLocker

Detail features –
https://github.com/svjt78/PasswordLocker/files/129976/PasswordLockerFeatures.docx



