package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

func Barber(_Visitors chan int, _Done chan int) {
	for {
		fmt.Println("Barber is sleeping")
		var Visitor = <-_Visitors
		time.Sleep(time.Second)
		fmt.Println("Visitor came, barber woke up and start work")
		time.Sleep(time.Second * 3)
		fmt.Println("Barber finish work")
		_Done <- Visitor
	}
}

func Visitor(_Visitors chan int, _Done chan int, _Wg *sync.WaitGroup, _Ind int) {
	fmt.Println("Visitor " + strconv.Itoa(_Ind) + " came and stay in queue")
	_Visitors <- _Ind
	var DoneInd = <- _Done
	fmt.Println("Visitor " + strconv.Itoa(DoneInd) + " is serviced and leave barber")
	_Wg.Done()
}

func main()  {
	var Visitors = make(chan int)
	var Done = make(chan int)

	var wg sync.WaitGroup
	wg.Add(3)

	go Barber(Visitors, Done)
	go Visitor(Visitors, Done, &wg, 1)
	time.Sleep(time.Second * 2)
	go Visitor(Visitors, Done, &wg, 2)
	time.Sleep(time.Second * 2)
	go Visitor(Visitors, Done, &wg, 3)

	wg.Wait()
}