(ns mini-finance-controlinator.feature.piggybank.piggy-bank-model)


(defn piggy-bank [uuid name description saved-value goal-value]
  {
   :piggy-bank/id          uuid
   :piggy-bank/name        name
   :piggy-bank/description description
   :piggy-bank/saved-value saved-value
   :piggy-bank/goal-value goal-value
   })