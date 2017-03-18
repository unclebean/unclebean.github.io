---
title: Haskell Tutorial
description: record some notes.
author: unclebean
author-email: yushio1984@gmail.com
author-github: unclebean
date-created: 2015-01-13
date-modified: 2015-01-13
date-published: 2015-01-13
headline:
in-language: en
keywords: haskell
---

##1. Init project##
		cabal init
		
##2. Define main function with file
		main-is:  src/Main.hs
	
##3. Import file into main file
		1. hs-source-dirs:      src
		2. declare moudle in file the first line
		3. module FileName where
		4. import FileName
		
##4. Main definition agreement
		Haskell groups top-level bindings into modules
		Default module name is Main, as programs start at function main in Main
		Except for Main, a module named M must reside in a file named M.hs
		Module names are capitalized; I use lower-case file names for Main modules
		
##5. Input arguments
		putStrLn "What;s your name?"
		name <- getLine
		print name

##6. Read Write Append file
		read:    content <- readFile "PATH"
		write:   writeFile "PATH" content
		append:  appendFile "PATH" new part
		
##7. List - operations
		1. An agreement
			Every element in a list must be same type.
		2. Get element from list follow index
			[1..] !! 5
		3. Join list
			[1..5] ++ [6]
		4. Get split list
			head,tail,last,init
		5. list comprehensions
			[x*2 | x<-[1..5]]
			[x*x | x<-[1..5], x*2>4]

##8. Tuple - operations
		1. An agreement
			Every element in a tuple must be fix length.
		2. Access elemets of a pair
			fst ("hello", "haskell")
			snd ("hello", "haskell")
		
##9. Define function
		Have two types to define a function.
		1. Defination a function with type.
		2. Signature function with TypeClass.

##10. TypeClass and Function
		TypeClass like agreement which means what kind of property can be assigned to the Type.
		TypeClass also can use to define a function that can be make the function flexible.
		
		[how to define a function with TypeClass]
		functionName :: (Eq a, Num a, Integral b) => a->b->b
		In the function definition the last 'b' means the function **return value of TypeClass.**

##11. Some TypeClass
		Eq
		Ord
		Show
		Read
		Enum
		Bounded
		Num
		Integral
		Floating
		
##12. lambda

##13. data types

##14. Monad class

##15. Maybe monad


		