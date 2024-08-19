package request

type CreateCategoryRequest struct {
	Name        string `json:"categoryName"`
	Description string `json:"description"`
}
