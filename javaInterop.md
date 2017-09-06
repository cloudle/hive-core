;; Mutatble syntax..
(doto
 (JFrame. "Foobar")
 (.add (proxy [JPanel] []))
 (.setSize 640 640)
 (.setVisible true))

