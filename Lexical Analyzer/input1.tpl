endAt = 7 // note an identifier is used without definition.
// Also note that there is no semicolon.
startFrom = 0
fib_last = 1
fib_secondLast = 1
while (startFrom < endAt) {
fib_next = fib_last + fib_secondLast
fib_secondLast = fib_last
fib_last = fib_next
startFrom = startFrom + 1
}
print(fib_last) //prints the value of seventh digit
// of the Fibonacci series