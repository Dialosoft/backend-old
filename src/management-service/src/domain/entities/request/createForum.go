package request

type CreateForum struct {
	Name        string `json:"name"`
	Description string `json:"description"`
	CategoryID  string `json:"categoryId"`
}
