/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-12.0.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: db
-- ------------------------------------------------------
-- Server version	12.0.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `answer_sheets`
--

DROP TABLE IF EXISTS `answer_sheets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_sheets` (
  `test_id` int(11) NOT NULL,
  `sequence` int(11) NOT NULL,
  `correct_option` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`test_id`,`sequence`),
  CONSTRAINT `answer_sheets_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `tests` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_sheets`
--

LOCK TABLES `answer_sheets` WRITE;
/*!40000 ALTER TABLE `answer_sheets` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `answer_sheets` VALUES
(1,1,'A'),
(1,2,'A'),
(1,3,'A'),
(1,4,'B'),
(1,5,'A'),
(1,6,'D'),
(1,7,'C'),
(1,8,'B'),
(1,9,'A'),
(1,10,'B'),
(1,11,'C'),
(1,12,'B'),
(1,13,'A'),
(1,14,'B'),
(1,15,'B'),
(1,16,'A'),
(1,17,'C'),
(1,18,'B'),
(1,19,'C'),
(1,20,'A'),
(1,21,'B'),
(1,22,'C'),
(1,23,'B'),
(1,24,'A'),
(1,25,'C'),
(1,26,'A'),
(1,27,'C'),
(1,28,'A'),
(1,29,'B'),
(1,30,'C'),
(1,31,'A'),
(1,32,'B'),
(1,33,'A'),
(1,34,'C'),
(1,35,'A'),
(1,36,'B'),
(1,37,'C'),
(1,38,'A'),
(1,39,'C'),
(1,40,'B'),
(1,41,'C'),
(1,42,'C'),
(1,43,'D'),
(1,44,'D'),
(1,45,'D'),
(1,46,'A'),
(1,47,'D'),
(1,48,'B'),
(1,49,'C'),
(1,50,'A'),
(1,51,'C'),
(1,52,'D'),
(1,53,'B'),
(1,54,'C'),
(1,55,'B'),
(1,56,'C'),
(1,57,'A'),
(1,58,'D'),
(1,59,'C'),
(1,60,'D'),
(1,61,'A'),
(1,62,'C'),
(1,63,'D'),
(1,64,'A'),
(1,65,'A'),
(1,66,'B'),
(1,67,'B'),
(1,68,'D'),
(1,69,'C'),
(1,70,'C'),
(1,71,'C'),
(1,72,'A'),
(1,73,'D'),
(1,74,'A'),
(1,75,'C'),
(1,76,'B'),
(1,77,'B'),
(1,78,'C'),
(1,79,'D'),
(1,80,'D'),
(1,81,'C'),
(1,82,'C'),
(1,83,'A'),
(1,84,'D'),
(1,85,'C'),
(1,86,'D'),
(1,87,'A'),
(1,88,'B'),
(1,89,'A'),
(1,90,'C'),
(1,91,'D'),
(1,92,'C'),
(1,93,'B'),
(1,94,'B'),
(1,95,'C'),
(1,96,'C'),
(1,97,'B'),
(1,98,'C'),
(1,99,'A'),
(1,100,'C'),
(1,101,'B'),
(1,102,'A'),
(1,103,'B'),
(1,104,'A'),
(1,105,'B'),
(1,106,'C'),
(1,107,'D'),
(1,108,'B'),
(1,109,'C'),
(1,110,'A'),
(1,111,'C'),
(1,112,'A'),
(1,113,'B'),
(1,114,'B'),
(1,115,'C'),
(1,116,'D'),
(1,117,'C'),
(1,118,'B'),
(1,119,'A'),
(1,120,'A'),
(1,121,'C'),
(1,122,'C'),
(1,123,'A'),
(1,124,'A'),
(1,125,'C'),
(1,126,'D'),
(1,127,'C'),
(1,128,'A'),
(1,129,'D'),
(1,130,'A'),
(1,131,'C'),
(1,132,'A'),
(1,133,'D'),
(1,134,'B'),
(1,135,'B'),
(1,136,'D'),
(1,137,'A'),
(1,138,'C'),
(1,139,'C'),
(1,140,'D'),
(1,141,'B'),
(1,142,'B'),
(1,143,'D'),
(1,144,'C'),
(1,145,'C'),
(1,146,'B'),
(1,147,'D'),
(1,148,'B'),
(1,149,'C'),
(1,150,'A'),
(1,151,'C'),
(1,152,'B'),
(1,153,'B'),
(1,154,'B'),
(1,155,'C'),
(1,156,'A'),
(1,157,'B'),
(1,158,'A'),
(1,159,'C'),
(1,160,'B'),
(1,161,'B'),
(1,162,'C'),
(1,163,'C'),
(1,164,'D'),
(1,165,'B'),
(1,166,'C'),
(1,167,'D'),
(1,168,'B'),
(1,169,'C'),
(1,170,'A'),
(1,171,'C'),
(1,172,'A'),
(1,173,'B'),
(1,174,'C'),
(1,175,'B'),
(1,176,'A'),
(1,177,'B'),
(1,178,'C'),
(1,179,'C'),
(1,180,'B'),
(1,181,'C'),
(1,182,'D'),
(1,183,'A'),
(1,184,'A'),
(1,185,'B'),
(1,186,'A'),
(1,187,'B'),
(1,188,'D'),
(1,189,'C'),
(1,190,'D'),
(1,191,'D'),
(1,192,'A'),
(1,193,'C'),
(1,194,'D'),
(1,195,'D'),
(1,196,'A'),
(1,197,'B'),
(1,198,'D'),
(1,199,'A'),
(1,200,'B');
/*!40000 ALTER TABLE `answer_sheets` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `blogs`
--

DROP TABLE IF EXISTS `blogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `blogs` (
  `blog_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `upvote` int(11) DEFAULT NULL,
  PRIMARY KEY (`blog_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `blogs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blogs`
--

LOCK TABLES `blogs` WRITE;
/*!40000 ALTER TABLE `blogs` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `blogs` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `flashcards`
--

DROP TABLE IF EXISTS `flashcards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `flashcards` (
  `flashcard_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `description` text DEFAULT NULL,
  `familiar_point` int(11) DEFAULT NULL,
  PRIMARY KEY (`flashcard_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `flashcards_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flashcards`
--

LOCK TABLES `flashcards` WRITE;
/*!40000 ALTER TABLE `flashcards` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `flashcards` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `test_id` int(11) NOT NULL,
  `question_number` int(11) NOT NULL,
  `part` int(11) DEFAULT NULL,
  `question_text` text DEFAULT NULL,
  `option_a` varchar(255) DEFAULT NULL,
  `option_b` varchar(255) DEFAULT NULL,
  `option_c` varchar(255) DEFAULT NULL,
  `option_d` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`test_id`,`question_number`),
  CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `tests` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `questions` VALUES
(1,32,1,'What are the speakers mainly discussing?','A training seminar.','The installation of a television.','The date of a presentation.','A software upgrade.\r'),
(1,33,1,'What is the problem?','The necessary tools are unavailable.','The office is closed.','The wall is too weak.','The phone number was wrong.\r'),
(1,34,1,'What most likely will the man do first tomorrow?','Order a replacement part.','Consult an instruction manual.','Contact the woman.','Fill out a work order.\r'),
(1,35,1,'What position is the man applying for?','Lecturer.','Editor.','Journalist.','Superintendent.\r'),
(1,36,1,'What makes the man qualified for the position?','His academic background.','His previous work experience.','His public popularity.','His eloquence.\r'),
(1,37,1,'What extra benefit does the woman mention?','Health insurance.','Flexible hours.','A lot of free time.','Regular incentives.\r'),
(1,38,1,'What are the speakers mainly discussing?','An interior renovation.','A product launch.','A luncheon reservation.','A budget proposal.\r'),
(1,39,1,'What does the man say about the dining room?','It needs more lighting.','It is quite cold.','It is spacious.','It is too loud.\r'),
(1,40,1,'What does the man suggest the woman do?','Repaint the walls a brighter color.','Compensate guests who have reservations.','Draft a budget proposal.','Open a bank account.\r'),
(1,41,1,'What is the man concerned about?','Getting his camera fixed.','Receiving sick leave from work.','Preparing for a party.','Introducing a client.\r'),
(1,42,1,'According to the man, why does Greg like his new job?','It offers better vacation time.','It pays a higher salary.','It matches his abilities.','It provides health benefits.\r'),
(1,43,1,'What most likely will the woman do next?','Take a group photo.','Attend a Christmas party.','Contact Greg.','Send an e-mail attachment.\r'),
(1,44,1,'What is the man concerned about?','Finishing a project on time.','Paying for his new mobile phone.','Repairing a piece of equipment.','Learning a new skill.\r'),
(1,45,1,'Where do the speakers work?','At a repair shop.','At an electronics store.','At a marketing firm.','At a design company.\r'),
(1,46,1,'What does the woman offer to do?','Provide assistance.','Pay in cash.','Fill in for the man.','E-mail a user manual.\r'),
(1,47,1,'Who most likely is the man?','A shop owner.','A construction worker.','A local resident.','A market researcher.\r'),
(1,48,1,'What does the woman mention about the mall?','It was recently renovated.','It has sufficient parking space.','It is attracting many tourists.','It is located outside of town.\r'),
(1,49,1,'Why does the woman usually visit the mall?','To purchase groceries.','To meet with her clients.','To buy clothing.','To deliver products.\r'),
(1,50,1,'What are the speakers discussing?','Orders for office supplies.','Equipment for a conference.','The budget reports.','Their colleague.\r'),
(1,51,1,'Why does the man mention when the supply company closes?','To inform her of the business hours.','To let her know she can\'t order anything.','To explain that the second order would be late.','To imply that new equipment can\'t be ordered.\r'),
(1,52,1,'What does the woman offer to do?','Pay for the new order.','Order the supplies herself.','Cancel a meeting.','Speak to their colleague.\r'),
(1,53,1,'What is the problem?','The plane tickets were not booked.','A meeting had to be rescheduled.','The meeting was a success.','A deadline has been changed.\r'),
(1,54,1,'Which part of the business trip will be postponed?','The meeting in New York.','The meeting in Wisconsin.','The meeting in Washington.','The meeting in Westboro.\r'),
(1,55,1,'What does the man mean when he says \"That\'s not a bad idea\"?','He thinks it is a bad idea.','He agrees with the proposed solution.','He wants to hear other ideas.','He disagrees with the solution.\r'),
(1,56,1,'What was the woman doing in Australia?','Going on a business trip.','Studying abroad.','Taking a vacation.','Searching for employees.\r'),
(1,57,1,'What does the woman imply when she says \"Is this Robert Wilder\'s application\"?','She is surprised to see the application.','She will reject the application.','She doesn\'t understand something.','She agrees with the application.\r'),
(1,58,1,'How does the woman know Robert Wilder?','They went to college together.','They work in the same department.','They play baseball together.','They play tennis together.\r'),
(1,59,1,'What is the woman concerned about?','Getting extra vacation.','Doing too much work.','Not having time for her children.','Preparing a report.\r'),
(1,60,1,'What does the man suggest?','Fire the manager.','Wait until their vacation.','Hire a babysitter.','Have some extra vacation days.\r'),
(1,61,1,'What does the woman say she will have to do?','Hire a babysitter.','Go to another company.','Ask her husband.','Finish her sales reports.\r'),
(1,62,1,'What does the woman have on Friday?','A dinner meeting.','A seminar.','A meeting.','A work party.\r'),
(1,63,1,'Look at the graphic. How much does the woman pay for the furniture?','$165.','$195.','$307.','$614.\r'),
(1,64,1,'What does the man say he will do?','Arrange free delivery.','Deliver the furniture in the evening.','Send a confirmation.','Deliver the table himself.\r'),
(1,65,1,'Why does the woman call?','To get an upgrade.','To book a flight to Korea and Japan.','To cancel her flight to Singapore.','To sign up for a mileage card.\r'),
(1,66,1,'Look at the graphic. How many points will the woman use?','50,000.','60,000.','70,000.','80,000.\"\r\n1,67,1,What suggestion does the man give the woman?,Upgrade her Korean flight.,Make the request after her trip.,Book a different flight.,Cancel her reservation.\r\n1,68,1,What are the speakers discussing?,Their GPS systems.,Which coffee shop to vi'),
(1,85,1,'What will they be sending a lot of?','Portfolios.','Contract forms.','Vital data.','Building plans.\r'),
(1,86,1,'What is \"The Tempest\" about?','The evolution of man.','A love story about a man and woman.','An action movie.','It\'s theme is magic and illusion.\r'),
(1,87,1,'Why does the speaker say, \"Remember, last year the Bromley Actors Guild won first place at this event\"?','To suggest that they are impressive.','To recommend that you join them.','To explain why they are here.','To excuse a poor performance.\r'),
(1,88,1,'What will most likely happen after the film screening?','Dinner and drinks.','Question time with the actors.','DVDs will be sold.','The actors will sign autographs.\r'),
(1,89,1,'What type of products are being discussed?','Cell phone cases and selfie sticks.','Cell phones and MP3 players.','Selfie sticks and headphones.','Software programs.\r'),
(1,90,1,'Why does the speaker say, \"I wonder if the cost is too high compared to the other products on the market\"?','To ask for assistance.','To offer help.','To suggest a change.','To create some new products.\r'),
(1,91,1,'What will the listeners most likely do after lunch?','Review safety policy.','Attend a seminar.','Go back to work.','Have a conference call.\r'),
(1,92,1,'Look at the graphic. Which items need to be ordered?','Office tables and chairs.','Chairs and drafting tables.','Whiteboards and office chairs.','Chairs and whiteboard.\r'),
(1,93,1,'What does the speaker anticipate about the company?','They won\'t need any more furniture.','They will have more staff in their building.','The boardrooms will be renovated.','Their staff are moving offices.\r'),
(1,94,1,'What is the listener asked to do before making any orders?','Sign them herself.','Make sure the manager signs them.','Bring some extra paper.','Prepare a delivery receipt.\r'),
(1,95,1,'Which industry does the speaker work in?','Computer hardware.','Computer games.','Computer software.','Computer microchips.\r'),
(1,96,1,'Look at the graphic. What company does the speaker work for?','Future Tech Studios.','Slight Line Inc.','Seven Strings Technologies.','AK Gaming.\r'),
(1,97,1,'According to the speaker, what will the company do in the next quarter?','Give away free gifts.','Give away expansion packs for free.','Offer free software with new products.','Install a new security system.\r'),
(1,98,1,'What are the listeners training to be?','Factory workers.','Store owners.','Restaurant chefs.','Medical workers.\r'),
(1,99,1,'According to the speaker, what will the listeners enjoy doing?','Working with the celebrity chefs.','Becoming a celebrity chef.','Using the kitchen tools.','Working with each other.\r'),
(1,100,1,'Look at the graphic. On what day will the listeners learn food safety and hygiene?','Tuesday.','Wednesday.','Thursday.','Friday.\r'),
(1,101,1,'When filling out the order form, please _____ your address clearly to prevent delays.','fix','write','send','direct\r'),
(1,102,1,'Ms. Morgan recruited the individuals that the company _____ for the next three months.','will employ','to employ','has been employed','employ\r'),
(1,103,1,'The contractor had a fifteen-percent _____ in his business after advertising in the local newspaper.','experience','growth','formula','incentive\r'),
(1,104,1,'The free clinic was founded by a group of doctors to give _____ for various medical conditions.','treatment','treat','treated','treating\r'),
(1,105,1,'Participants in the walking tour should gather _____ 533 Bates Road on Saturday morning.','with','at','like','among\r'),
(1,106,1,'The artist sent _____ best pieces to the gallery to be reviewed by the owner.','him','himself','his','he\r'),
(1,107,1,'The figures that accompany the financial statement should be _____ to the spending category.','relevance','relevantly','more relevantly','relevant\r'),
(1,108,1,'The building owner purchased the property _____ three months ago, but she has already spent a great deal of money on renovations.','yet','just','few','still\r'),
(1,109,1,'We would like to discuss this problem honestly and _____ at the next staff meeting.','rarely','tiredly','openly','highly\r'),
(1,110,1,'The store\'s manager plans to put the new merchandise on display _____ to promote the line of fall fashions.','soon','very','that','still\r'),
(1,111,1,'During the peak season, it is _____ to hire additional workers for the weekend shifts.','necessitate','necessarily','necessary','necessity\r'),
(1,112,1,'_____ that the insulation has been replaced, the building is much more energy-efficient.','Now','For','As','Though\r'),
(1,113,1,'Mr. Sims needs a more _____ vehicle for commuting from his suburban home to his office downtown.','expressive','reliable','partial','extreme\r'),
(1,114,1,'The company _____ lowered its prices to outsell its competitors and attract more customers.','strategy','strategically','strategies','strategic\r'),
(1,115,1,'_____ Mr. Williams addressed the audience, he showed a brief video about the engine he had designed.','Then','So that','Before','Whereas\r'),
(1,116,1,'For optimal safety on the road, avoid _____ the view of the rear window and side-view mirrors.','obstructs','obstructed','obstruction','obstructing\r'),
(1,117,1,'Having proper ventilation throughout the building is _____ for protecting the health and well-being of the workers.','cooperative','visible','essential','alternative\r'),
(1,118,1,'_____ sales of junk food have been steadily declining indicates that consumers are becoming more health-conscious.','In addition to','The fact that','As long as','In keeping with\r'),
(1,119,1,'The sprinklers for the lawn\'s irrigation system are _____ controlled.','mechanically','mechanic','mechanism','mechanical\r'),
(1,120,1,'The library staff posted signs to _____ patrons of the upcoming closure for renovations.','notify','agree','generate','perform\r'),
(1,121,1,'Mr. Ross, _____ is repainting the interior of the lobby, was recommended by a friend of the building manager.','himself','he','who','which\r'),
(1,122,1,'The guidelines for the monthly publication are _____ revised to adapt to the changing readers.','courteously','initially','periodically','physically\r'),
(1,123,1,'_____ an ankle injury, the baseball player participated in the last game of the season.','In spite of','Even if','Whether','Given that\r'),
(1,124,1,'The governmental department used to provide financial aid, but now it offers _____ services only.','legal','legalize','legally','legalizes\r'),
(1,125,1,'At the guest\'s_____, an extra set of towels and complimentary soaps were brought to the room.','quote','graduation','request','dispute\r'),
(1,126,1,'The upscale boutique Jane\'s Closet is known for selling the most stylish _____ for young professionals.','accessorized','accessorize','accessorizes','accessories\r'),
(1,127,1,'The company started recognize the increasing _____ of using resources responsibly.','more important','importantly','importance','important\r'),
(1,128,1,'_____ restructuring several departments within the company, the majority of the problems with miscommunication have disappeared.','After','Until','Below','Like\r'),
(1,129,1,'The riskiest _____ of the development of new medications are the trials with human subjects.','proceeds','perspectives','installments','stages\r'),
(1,130,1,'_____ seeking a position at Tulare Designs must submit a portfolio of previous work.','Anyone','Whenever','Other','Fewer\r'),
(1,131,1,'','seek','to seek','seeking','are seeking\r'),
(1,132,1,'','extensive','restricted','generous','limitless\r'),
(1,133,1,'','I would really appreciate the opportunity to work with you.','I heard that DigitalIT is a great company.','In fact, our designs are often copied by other companies.','I have attached a number of our past designs to illustrate what we specialize in.\r'),
(1,134,1,'','at','to','with','from\r'),
(1,135,1,'','durable','durability','duration','during\r'),
(1,136,1,'','Larson\'s utensils and silverware go great with the dinnerware.','Our most popular line, the Spring Flower China is sold out at most locations.','Visit our store to check out our other beautiful products.','They are dishwasher- and microwave-safe and we\'re confident that you\'ll be using them for years to come.\r'),
(1,137,1,'','result in','occur to','ending at','stop with\r'),
(1,138,1,'','ambitious','combative','aggressive','complacent\r'),
(1,139,1,'','account','accountant','accounting','accounted\r'),
(1,140,1,'','basic','decisive','additional','necessary\r'),
(1,141,1,'','is being','will be','has been','were being\r'),
(1,142,1,'','We sincerely thank you for your interest.','The positions begin the following month.','Please call us for more information.','We apologize for any inconvenience.\r'),
(1,143,1,'','are bringing in','have brought in','bring in','are brought in\r'),
(1,144,1,'','This will take a lot of work.','As a result, the convenience shops will be closed.','Because of this, hot meals will not be available for the patrons.','There will be noise and chaos as a result.\r'),
(1,145,1,'','before','after','during','within\r'),
(1,146,1,'','develop','improve','rectify','recover\r'),
(1,147,1,'What is indicated about the seminar?','It will feature speaker James Taylor.','It is held annually.','Its fee is more expensive than the last one.','It is designed for women.\r'),
(1,148,1,'When will the free registration offer end?','On February 5','On February 12','On February 21','On February 23\r'),
(1,149,1,'Where most likely is Nancy?','At a conference room','At the IT department','In the supply room','In her office\r'),
(1,150,1,'What did Nancy mean when she said \"I\'m headed there now\"?','She was going to the location.','She would lead the presentation.','She knew where the room was.','She was going straight to meet him.\r'),
(1,151,1,'What is Mr. Bailey advised to do?','Contact the theater for a refund','Select his preferred seat on a website','Arrive at the venue in advance','Post a review later\r'),
(1,152,1,'According to the ticket, what can be viewed on the theater\'s website?','A list of past performances','Driving directions','Concert reviews','Pictures of the theater\r'),
(1,153,1,'What kind of business are the items most likely intended for?','A shopping mall','A bakery','An appliance store','A convenience store\r'),
(1,154,1,'What is indicated about the order?','It will be paid in installments.','It will be sent separately.','It has been discounted.','It will be delivered at no charge.\r'),
(1,155,1,'What is indicated about the old courthouse?','Branford Construction wants to renovate the building.','The residents want to turn the building into a shopping mall.','It may become a public library or school.','It may be destroyed.\r'),
(1,156,1,'In which of the positions marked [1], [2], [3] and [4] does the following sentence belong? \"Branford Construction, the development company that originally planned to build the shopping mall, is looking to build the mall outside of the Rivervalley Community.\"','[1]','[2]','[3]','[4]\r'),
(1,157,1,'What is suggested about the fundraising efforts?','The community made a lot of money from the land.','It has been occuring online.','The city government has been helping.','They haven\'t raised enough money.\r'),
(1,158,1,'Who most likely is Ms. Johnson?','A store manager','A customer','A product developer','A marketing specialist\r'),
(1,159,1,'What is indicated about Topline Electronics?','It recently opened a new store location.','It will be relocated to the Crayville area.','It is concerned about market competition.','It was nominated for an annual award.\r'),
(1,160,1,'What is Ms. Johnson NOT instructed to do?','Add new information to a website','Hire additional staff','Contact Management if necessary','Arrange a sale display area\r'),
(1,161,1,'What kind of business do the online speakers work at?','A law firm','An office supply company','A furniture shop','A moving company\r'),
(1,162,1,'When will the crew begin work?','Tuesday','Wednesday','Thursday','Friday\r'),
(1,163,1,'What will Johnny Jordan probably do next?','Contact the distributors','Organize a meeting','Gather a large crew','Call the client\r'),
(1,164,1,'What does Monica Stein mean by \"I\'m on it\"?','She\'ll organize the movers.','She\'ll wait until she gets more information.','She\'ll visit the manufacturers.','She\'ll contact the client.\r'),
(1,165,1,'Who is most likely to be a customer of Fulton Stainless Steel Products?','A car manufacturer','A restaurant','A real estate agency','A clothing store\r'),
(1,166,1,'What did Fulton Stainless Steel Products do last month?','It held a press conference.','It opened a new factory.','It obtained a company.','It laid off some workers.\r'),
(1,167,1,'What is mentioned about Fulton Stainless Steel Products?','It recently provided extra funding for research and development.','It has released a budget proposal for next year.','It offers a benefits package to its part-time employees.','It plans to hire additional employees to work in factories.\r'),
(1,168,1,'Why most likely was the article written?','To introduce a new employee','To report on an award winner','To announce an annual competition','To describe a change in company policy\r'),
(1,169,1,'The word \"founded\" is closest in meaning to','discovered','learned','established','equipped\r'),
(1,170,1,'What is suggested about the Association of Web Designers?','It holds a conference every year.','It is based in Los Angeles.','It currently offers free membership.','It donates to community projects.\r'),
(1,171,1,'According to the article, what can be found on the website?','A transcript of a speech','An application for an open position','Details about upcoming contests','A list of Ms. Holt\'s accomplishments\r'),
(1,172,1,'What is suggested about the shop?','It is very successful.','It only offers take-out.','It has been open for a long time.','Only fitness experts patron the shop.\r'),
(1,173,1,'What is suggested about Health Shack products?','They are very delicious.','They are healthy.','They are cheap.','They are easy to get.\r'),
(1,174,1,'Why don\'t the owners advertise?','They don\'t have enough money.','They are too busy.','Their customers recommend the place to others.','They don\'t want to.\r'),
(1,175,1,'In which of the positions marked [1], [2], [3] and [4] does the following sentence belong? \"Despite the limited number of products on the menu, customers can\'t get enough of the tasty but healthy shakes that are on offer.\"','[1]','[2]','[3]','[4]\r'),
(1,176,1,'Who most likely is the lecture series intended for?','Community members','Building superintendents','University professors','Hospital patients\r'),
(1,177,1,'In the brochure, the word \"through\" is closest in meaning to','over','via','across','until\r'),
(1,178,1,'What most likely will be discussed at the lecture on February 10?','How to balance yearly budgets','How to meet infrastructure needs','How to avoid environmental damage','How to stimulate economic development\r'),
(1,179,1,'According to Mr. Patterson, whose lecture must be rescheduled?','Mr. Watson\'s','Mr. Ross\'s','Ms. Simmons\'s','Mr. Powell\'s\r'),
(1,180,1,'What is Ms. Flores instructed to do on a website?','Download a document','Change a room reservation','Update personal information','Facilitate a forum\r'),
(1,181,1,'What can be inferred about Ms. Russell?','She is Ms. Bennett\'s co-worker.','She is planning a honeymoon.','She is currently engaged.','She works for a catering company.\r'),
(1,182,1,'Who most likely is Ms. Bennett?','A professional musician','A wedding photographer','An interior designer','An event planner\r'),
(1,183,1,'What is suggested about all the locations on the list?','They are close to a main road.','They are indoor venues.','They are located in the same city.','They require a down payment.\r'),
(1,184,1,'What information is Ms. Russell asked to provide?','A potential date','A meal selection','A list of guests','A meeting location\r'),
(1,185,1,'What package would probably best suit Ms. Russell\'s needs?','Diamond','Emerald','Ruby','Sapphire\r'),
(1,186,1,'What is the purpose of the first e-mail?','To request cost information','To inquire about a policy change','To postpone an order','To report an incorrect invoice\r'),
(1,187,1,'What package option most likely fits Ms. Hall\'s needs best?','Personal','Small','Medium','Large\r'),
(1,188,1,'What information is not needed for a final price?','Length of contract','Method of delivery','Additional items','Distance of shipping\r'),
(1,189,1,'What is indicated in Emily Hall\'s e-mail?','She wants to try it for a month.','She wants the free gift.','She is interested in a long-term contract.','She doesn\'t want winter produce.\r'),
(1,190,1,'Why does Emily Hall want to know about the vegetables that will be available throughout the year?','She loves vegetables.','She is thinking about adding meat.','She might hire another employee.','She wants to plan her future menus.\r'),
(1,191,1,'Where would the notice most likely be found?','In a restaurant','In a staff break room','In a shipping agency','In a grocery store\r'),
(1,192,1,'What is indicated about the jars?','They were not closed tightly.','They are currently out of stock.','They were priced incorrectly.','They were delivered to the wrong address.\r'),
(1,193,1,'According to the notice, what is NOT mentioned as advice for customers?','Avoiding consuming the product','Reporting on the product','Returning the product to a store','Purchasing other Castelli products\r'),
(1,194,1,'In the e-mail to Mr. Hester, what additional gift does Castelli offer?','Pasta sauce','A recipe book','Vouchers for produce','Vouchers for new products\r'),
(1,195,1,'Castelli is sending the vouchers for a several reasons; what is NOT one of them?','To keep customers loyal','To say that they were sorry','To be fair to their customers','To gain new customers\r'),
(1,196,1,'Why was the inspection conducted?','To monitor compliance with food industry regulations','To rate the taste and quality of the cuisine','To inspect the structural safety of the building','To evaluate the effectiveness of new policies\r'),
(1,197,1,'Why has Polito\'s Pizza been charged a fine?','Because fire extinguishers were not in place.','Because containers of food were not marked appropriately.','Because raw meats and vegetables were handled incorrectly.','Because the facilities were not cleaned according to standards.\r'),
(1,198,1,'What does Mr. Kluck ask his employees to do?','Apologize to customers','Wear a name tag at all times','File a complaint with Ms. Tenner','Fill out a required form\r'),
(1,199,1,'What will happen if an employee fails to sign the work checklist?','They will have a violation on their record.','They will have to pay a fine.','They will have to come in on the weekends.','They will be fired.\r'),
(1,200,1,'Based on Polito\'s Checklist, what can we infer about K.P.?','He works at night.','He did not work on Thursday and Friday.','He will be fired for violations.','He is slow at work.\r');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `refs`
--

DROP TABLE IF EXISTS `refs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `refs` (
  `test_id` int(11) NOT NULL,
  `ref_id` int(11) NOT NULL,
  `ref_type` varchar(30) DEFAULT NULL,
  `path` text DEFAULT NULL,
  `start` int(11) DEFAULT NULL,
  `end` int(11) DEFAULT NULL,
  PRIMARY KEY (`test_id`,`ref_id`),
  CONSTRAINT `refs_ibfk_1` FOREIGN KEY (`test_id`) REFERENCES `tests` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refs`
--

LOCK TABLES `refs` WRITE;
/*!40000 ALTER TABLE `refs` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `refs` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `takes`
--

DROP TABLE IF EXISTS `takes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `takes` (
  `take_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `test_id` int(11) NOT NULL,
  `attempt_no` int(11) NOT NULL,
  `start_time` datetime DEFAULT current_timestamp(),
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`take_id`),
  UNIQUE KEY `user_id` (`user_id`,`test_id`,`attempt_no`),
  KEY `test_id` (`test_id`),
  CONSTRAINT `takes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `takes_ibfk_2` FOREIGN KEY (`test_id`) REFERENCES `tests` (`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `takes`
--

LOCK TABLES `takes` WRITE;
/*!40000 ALTER TABLE `takes` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `takes` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `test_results`
--

DROP TABLE IF EXISTS `test_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_results` (
  `result_id` int(11) NOT NULL AUTO_INCREMENT,
  `take_id` int(11) NOT NULL,
  `score` int(11) DEFAULT NULL,
  `taken_on` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`result_id`),
  KEY `take_id` (`take_id`),
  CONSTRAINT `test_results_ibfk_1` FOREIGN KEY (`take_id`) REFERENCES `takes` (`take_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_results`
--

LOCK TABLES `test_results` WRITE;
/*!40000 ALTER TABLE `test_results` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `test_results` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests` (
  `test_id` int(11) NOT NULL AUTO_INCREMENT,
  `test_name` varchar(100) DEFAULT NULL,
  `test_date` date NOT NULL,
  `total_questions` int(11) NOT NULL,
  `max_score` int(11) DEFAULT 990,
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests`
--

LOCK TABLES `tests` WRITE;
/*!40000 ALTER TABLE `tests` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `tests` VALUES
(1,NULL,'2025-09-03',200,990);
/*!40000 ALTER TABLE `tests` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `user_answers`
--

DROP TABLE IF EXISTS `user_answers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_answers` (
  `answer_id` int(11) NOT NULL AUTO_INCREMENT,
  `take_id` int(11) NOT NULL,
  `question_number` int(11) NOT NULL,
  `selected_option` char(1) NOT NULL,
  `is_correct` tinyint(1) NOT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `take_id` (`take_id`),
  CONSTRAINT `user_answers_ibfk_1` FOREIGN KEY (`take_id`) REFERENCES `takes` (`take_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_answers`
--

LOCK TABLES `user_answers` WRITE;
/*!40000 ALTER TABLE `user_answers` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `user_answers` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `gender` tinyint(1) DEFAULT NULL,
  `avatar_data` longblob,
  `avatar_content_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-09-13  8:57:10
