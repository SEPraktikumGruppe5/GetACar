GetACar
=======

This is the official Get A Car Git repository

News:

A lot has happened!

The account app uses 'ui-router'. You should read about it, as I think it's the best fit for the main app, too.

We now have two Maven profiles! One is 'development' and the other one is 'production'.

Profile 'development' is active by default. It does not compress, minify, uglify etc. the code while 'production'
profile does. To use the 'production' profile execute the maven target with the parameters '-P production,-development'.
IDEs should have built-in support for profiles, too.

Don't forget to import the SQL script once, because the users in your database are likely set to inactive.

After the redeploy & import of the SQL script navigate to 'http://localhost:8080/getacar/app' to test! You should get
redirected after a successful login! (admin:admin / user:user are valid username:password combinations!)

Not ready in the account app yet:
- Error messages on failed login attempts
- Forward after registration (Not sure if registration works actually)
- Server generates english messages on an OS with english locale, do we really want il8n?
- Styling badly needed + a concept of the basic layout! (Fixed footer? Fixed width? Colors? CSS-Framework? ...)