MovBase
=======
prihlasenie cez facebook
- pridany LoginController
- kvoli testovaniu boli upravene index.jsp, IndexController (to co je oznacene medzi komentarmi)
- k session sa da pristupovat v metodach, ktore maju parameter HttpServletReqeust - 
   - request.getSession().getAttribute("attr") - atributu su userId - id prihl. pouz. a accessToken
- treba pridat kniznice (su na googli v priecinku "jars")
--------------------------------------------------------------------------------------------------
rest services
- 4 sluzby - vsetky filmy/film podla id/vsetci herci/herec podla id
- vracia sa xml
- neviem ci to staci takto aby to bolo spravene
- treba pridat jax-ri - je tie na google
