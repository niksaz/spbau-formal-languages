proc write_fib(n) {
    x := 1
    y := 1
    while (n > 2) {
        tmp := x + y
        x := y
        y := tmp
        n := n - 1
    }
    write(y)
}

n := 0
read(n)
write_fib(n)