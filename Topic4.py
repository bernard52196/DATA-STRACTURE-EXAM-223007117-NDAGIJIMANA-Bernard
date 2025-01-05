import heapq

class Order:
    def __init__(self, order_id, material_name, quantity, priority):
        self.order_id = order_id
        self.material_name = material_name
        self.quantity = quantity
        self.priority = priority

    def __lt__(self, other):
        return self.priority < other.priority

    def __str__(self):
        return f"Order ID: {self.order_id}, Material: {self.material_name}, Quantity: {self.quantity}, Priority: {self.priority}"


class OrderHeap:
    def __init__(self, max_size):
        self.max_size = max_size
        self.heap = []

    def add_order(self, order):
        if len(self.heap) < self.max_size:
            heapq.heappush(self.heap, order)
            print(f"Order {order.order_id} added to the heap.")
        else:
            print(f"Heap full! Replacing the lowest priority order if necessary.")
            if order.priority > self.heap[0].priority:
                removed = heapq.heappop(self.heap)
                heapq.heappush(self.heap, order)
                print(f"Replaced Order {removed.order_id} with Order {order.order_id}.")
            else:
                print(f"Order {order.order_id} not added due to low priority.")

    def view_orders(self):
        if not self.heap:
            print("No orders in the heap.")
        else:
            print("Current Orders in the Heap (sorted by priority):")
            for order in sorted(self.heap, key=lambda x: -x.priority):
                print(order)

    def remove_highest_priority_order(self):
        if self.heap:
            highest_priority_order = heapq.heappop(self.heap)
            print(f"Removed Order {highest_priority_order.order_id} (highest priority).")
        else:
            print("Heap is empty. No order to remove.")



order_heap = OrderHeap(max_size=5)

order_heap.add_order(Order(1, "Cement", 100, 3))
order_heap.add_order(Order(2, "Steel", 50, 5))
order_heap.add_order(Order(3, "Bricks", 200, 2))
order_heap.add_order(Order(4, "Sand", 150, 4))
order_heap.add_order(Order(5, "Gravel", 80, 1))
order_heap.add_order(Order(6, "Tiles", 40, 6))

order_heap.view_orders()

order_heap.remove_highest_priority_order()
order_heap.view_orders()
