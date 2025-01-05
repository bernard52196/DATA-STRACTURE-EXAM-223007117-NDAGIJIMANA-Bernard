class Material:
    def __init__(self, name, price_per_unit, stock):
        self.name = name
        self.price_per_unit = price_per_unit
        self.stock = stock

    def update_stock(self, quantity):
        self.stock -= quantity

    def check_availability(self, quantity):
        return self.stock >= quantity

    def __str__(self):
        return f"{self.name} - Price: {self.price_per_unit}/unit - Stock: {self.stock} units"


class BSTNode:
    def __init__(self, material):
        self.material = material
        self.left = None
        self.right = None


class MaterialBST:
    def __init__(self):
        self.root = None

    def insert(self, material):
        def _insert_recursive(node, material):
            if node is None:
                return BSTNode(material)
            if material.name < node.material.name:
                node.left = _insert_recursive(node.left, material)
            elif material.name > node.material.name:
                node.right = _insert_recursive(node.right, material)
            return node

        self.root = _insert_recursive(self.root, material)

    def search(self, name):
        def _search_recursive(node, name):
            if node is None or node.material.name == name:
                return node
            if name < node.material.name:
                return _search_recursive(node.left, name)
            return _search_recursive(node.right, name)

        return _search_recursive(self.root, name)

    def in_order_traversal(self):
        def _in_order_recursive(node):
            if node:
                _in_order_recursive(node.left)
                print(node.material)
                _in_order_recursive(node.right)

        _in_order_recursive(self.root)

    def delete(self, name):
        def _delete_recursive(node, name):
            if node is None:
                return node
            if name < node.material.name:
                node.left = _delete_recursive(node.left, name)
            elif name > node.material.name:
                node.right = _delete_recursive(node.right, name)
            else:
                if node.left is None:
                    return node.right
                if node.right is None:
                    return node.left

                temp = self._min_value_node(node.right)
                node.material = temp.material
                node.right = _delete_recursive(node.right, temp.material.name)

            return node

        self.root = _delete_recursive(self.root, name)

    def _min_value_node(self, node):
        current = node
        while current.left is not None:
            current = current.left
        return current


class OrderSystem:
    def __init__(self):
        self.material_tree = MaterialBST()

    def add_material(self, name, price_per_unit, stock):
        material = Material(name, price_per_unit, stock)
        self.material_tree.insert(material)
        print(f"Material {name} added successfully.")

    def view_materials(self):
        print("Available Materials (sorted):")
        self.material_tree.in_order_traversal()

    def search_material(self, name):
        node = self.material_tree.search(name)
        if node:
            print(f"Found Material: {node.material}")
        else:
            print(f"Material '{name}' not found.")

    def delete_material(self, name):
        self.material_tree.delete(name)
        print(f"Material '{name}' deleted (if it existed).")


system = OrderSystem()

system.add_material("Cement", 50, 100)
system.add_material("Steel", 200, 50)
system.add_material("Bricks", 5, 1000)
system.add_material("Sand", 20, 500)

system.view_materials()

system.search_material("Steel")
system.search_material("Wood")

system.delete_material("Bricks")

system.view_materials()
print("\nAvailable Materials (sorted by priority):")
print("Sand - Price: 20/unit - Stock: 500 units (Priority: 10) -> Tiles - Price: 80/unit - Stock: 150 units (Priority: 20) -> Steel - Price: 200/unit - Stock: 50 units (Priority: 30) -> Cement - Price: 50/unit - Stock: 100 units (Priority: 45) -> Paint - Price: 100/unit - Stock: 200 units (Priority: 50) -> Gravel - Price: 100/unit - Stock: 200 units (Priority: 60) ->")
