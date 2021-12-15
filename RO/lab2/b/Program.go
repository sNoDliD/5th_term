package main

import (
	"fmt"
	"time"
)

func Ivanov(Storage chan int, ToPetrov chan int) {
	for true {
		var x = <-Storage
		fmt.Println("Ivanov: Recieved")
		time.Sleep(time.Second);
		fmt.Println("Ivanov: Done")
		ToPetrov <- x
	}
}

func Petrov(ToPetrov chan int, ToNecheporchuk chan int) {
	for true {
		var x = <-ToPetrov
		fmt.Println("Petrov: Recieved")
		time.Sleep(time.Second)
		fmt.Println("Petrov: Done")
		ToNecheporchuk <- x
	}
}

func Necheporchuk(ToNecheporchuk chan int, Result * int) {
	for true {
		var x = <-ToNecheporchuk
		fmt.Println("Necheporchuk: Recieved")
		time.Sleep(time.Second)
		fmt.Println("Necheporchuk: Done")
		*Result += x
	}
}

func main() {
	var Size = 5
	var Storage = make(chan int, Size)
	for i := 1; i <= Size; i++ {
		Storage <- i
	}

	var IvanovToPetrov = make(chan int, Size)
	var PetrovToNecheporchuk = make(chan int, Size)
	var Result int

	go Ivanov(Storage, IvanovToPetrov)
	go Petrov(IvanovToPetrov, PetrovToNecheporchuk)
	go Necheporchuk(PetrovToNecheporchuk, &Result)

	time.Sleep(time.Second * 10)
	fmt.Println(Result)
}
