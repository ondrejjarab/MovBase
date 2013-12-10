MovBase
=======
prihlasenie cez facebook
- pridany LoginController
- kvoli testovaniu boli upravene index.jsp, IndexController (to co je oznacene medzi komentarmi)
- k session sa da pristupovat v metodach, ktore maju parameter HttpServletReqeust - 
   - request.getSession().getAttribute("attr") - atributu su userId - id prihl. pouz. a accessToken
- treba pridat kniznice (su na googli v priecinku "jars")
--------------------------------------------------------------------------------------------------
