# Challenge

## :computer: How to execute
Used endpoints throughout calling rest controller annotations to be able to call endpoints for posting payment, saving
logs or validating payment.

## :memo: Notes
- Created models package (Account,Error,Payment) so as to fetch/add data from/to a certain database.
- Built an offline package that included repository,service,controller(rest controller).
- Set offline service to consume kafka data from producer by invoking the method(saveConsumedData)followed by this annotation
@KafkaListener(topics = Constant.TOPIC_OFFLINE) to save the payment data to the database successfully.
After then, I created restcontroller class to be able to set up an endpoint for calling the previous step(saveConsumedData method) to be invoked directly through postman.
-Built an online package that included repository,service,controller(rest controller). In this package,
I created a class annotated with @service to be able to validate the payment data. After then, I built a rest controller
class to be able to invoke this method through another method annotated with GetMapping annotation in the rest
controller class to check if it is valid or not through this endpoint.
-Built log package that included repository,service,controller(rest controller). With the service class, A method
has been created to save any error that occurs in the other two services (online,offline) then the rest controller
has been set to track a certain payment through a newly created endpoint to display this data by passing the 
payment id in the request.

## :pushpin: Things to improve

Unfortunately, I found out that I have a slow processing issue in my environment that hinders me from running the entire project thoroughly and efficiently but at the same 
time, I ordered another one and it is supposed to be delivered by the end of this month so if there is a possibility to resend the project with other needed modifications after a few days,
I would be highly appreciated.