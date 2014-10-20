Karaf based Web Application deployed on Heroku
===============================================

Work in progress
----------------

This is an Web application developed on Apache Karaf, deployed on Heroku.
You can see it live; [http://rezolva-tva.herokuapp.com/](http://rezolva-tva.herokuapp.com/)

You can use it to do (batch) VAT Number validation using [http://ec.europa.eu/taxation_customs/vies/vatRequest.html](http://ec.europa.eu/taxation_customs/vies/vatRequest.html).
It uses JAX-RS to deploy a web service that reads a list of company identification numbers (Romania) and performs SOAP calls to validate them.