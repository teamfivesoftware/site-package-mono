# Site Package

This service is designed to serve as a package tracking software for residential complexes that recieve packages for their residents in a front office. It is intended to serve as a concise service that you can use to track packages in and out, look up packages, sort by name, sender, etc, and eventually send notifications to residents that they have packages to be picked up.

## Jargon
- A **Data Stack** is the Data Model, Controller, Service, and Repository for a specific db table.

## Feature Scope
### Package Traffic
- Check packages in when they arrive; enter the resident's name, apartment number, date (defaults to today, but can be changed for packages received at end of day or on weekends, for example), courier (optional) and tracking number (optional) to store a package.
- Check packages out when they are picked up; enter who picked up the package, the date they did so, and certify that you viewed their ID befre giving out the package (feature can be disabled in the settings of the app).
- Mark packages as 'Incoming - Return to Sender' for packages received at the office that need to be sent back
- Packages automatically shown as 'Return to Sender' for packages outside of a daterange provided by the settings file (i.e. 'If packages are here for longer than 90 days, mark them for return')

### Data Look-Up
- Look up packages for a resident by name
- Look up packages by a specified date (i.e. 'Show me all packages from 3/1/2025')
- Look up packages by a specified courier (i.e. 'Show me all packages from FedEx Ground')
- X-Search; search using any of the above specified criteria (i.e. show me packages for John Doe on 3/1/2025 that shipped with FedEx Ground or USPS)
- 
### Import and Export
- Import resident names into the app using a .csv file. Templates will be available within the app, so you can download the template, fill it in, and upload it.
- Integrations with Entrata and AppFolio; Use a rent roll or other consolidated report to import resident data.
- Export packages to a .csv and/or pdf file
- Export database for easy hookup to new instance (i.e. if you want to boot the app on a new computer, it will have a fresh database, or you can upload an exported database file to 'reinstall' all your prexisting packages)

### Multiple Property Configuration
- View packages for multiple properties. You'll be able to view packages for one of your properites, or search across all properties you own that are stored in your database. For example, if I have Foo Bar on Main Street and Foo Bar on Cherry Street, I can search for packages just in Main Street, just in Cherry Street, or do a X-Search and search amongst both of properties.

### Account Management
- Each person that uses the account will sign in and sign out of the app.
- Each time the person checks-in and checks-out a pacakge, that person's name is added to the record.
- Read permissions are established based on location (i.e. John Doe works at Site A and can only see Site A etc.) Multiple work location associations can be created.

### Admin Permissions
- Sysadmin (developers) can configure a 'Site Admin' that gives varying levels of permissions to users. Site Admins can configure a pin or password to grant temporary, one-time permissions to certain features.
- By default, a normal user will not be able to delete packages that are entered in error without a Site Admin password/pin; Site Admin accounts can bypass this.
- Site Admins will be able to add/register new users to the service.
- Site Admins will be able to manage site associations for their staff
- Site Admins will be able to manage their staff's passwords, resetting it to a temp password in the event they've forgotten it.
- Site Admins will have complete access to the audit logs.
- Site Admins can add new properties and couriers to the DB.

### Audit Logs
- Site Admins will have access to the audit logs for properites. These logs will have ***ALL*** edits made to the database. Below is a list of all things that are included in the audit logs:
- Packages in
- Packages out
- Log ins
- Log outs
- Password resets
- Admin codes entered
- Packages deleted
- Courier added
- Courier deleted
- Property added
- Property deleted
- Property edited

## Timeline
### Phase Zero (CURRENT)
- Planning Phase.
- Timeline TBD.
  
### Phase One
- Roughly 4 weeks.
- Implement rough wireframes and basic features for the app.
  
### Phase Two
- Roughly 4 weeks.
- Implement finer details, get MVP finalised.
  
### Closed Alpha
- MVP rolls out to Closed Alpha testers.
- Trial Phase of 2 weeks.
  
### Phase Three
- Closed Alpha feedback integration loop.
- Timeline TBD.
  
### Stress Test 1
- New changes added to MVP, MVP rolled out to Closed Alpha testers.
- Timeline TBD.
  
### Phase Four
- Stress Test feedback integration loop.
- Timeline TBD.
  
### Stress Test 2
- New changes added to MVP, MVP rolled out to Closed Alpha testers.
- Timeline TBD.

### Phase 5
- Final changes and release preparation.
- Minium 2 weeks phase, could be longer depending on technical specifications.

### Release
- Site Package is released to the general public.

### RFE Cycle and On-Going support
- Site Package undergoes the Request for Enhancement (RFE) cycle and on-going support begins.
- Indefinite phase.

## Stack
- Electron and React parter to make a desktop app with enhanced capabilities.
- SQLite serves as the DB for the desktop app
- SpringBoot with Java makes up the API for DB interfacing
- Maven for backend packages
- NPM for frontend packages
- 
## Planned Additions
### Messaging (Paid)
- Send messages via email or SMS to notify residents that they have a package

### Webapp (Paid)
- Create a web-app version that utilises AWS to host a remote database that syncs with local databases

### Scanning (Paid)
- Develop a basic mobile app that would enable scanning packages and instantly importing them to your database.

